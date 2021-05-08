package com.kakarote.examine.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.util.TypeUtils;
import com.kakarote.core.common.cache.CrmCacheKey;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.entity.AdminMessageBO;
import com.kakarote.core.feign.admin.entity.AdminMessageEnum;
import com.kakarote.core.feign.admin.entity.SimpleUser;
import com.kakarote.core.feign.admin.service.AdminMessageService;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.feign.crm.service.CrmExamineService;
import com.kakarote.core.feign.examine.entity.ExamineGeneralBO;
import com.kakarote.core.feign.examine.entity.ExamineMessageBO;
import com.kakarote.core.feign.examine.entity.ExamineRecordReturnVO;
import com.kakarote.core.feign.examine.entity.ExamineRecordSaveBO;
import com.kakarote.core.feign.hrm.entity.HrmSalaryMonthRecord;
import com.kakarote.core.feign.hrm.service.SalaryRecordService;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.BaseUtil;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.examine.constant.*;
import com.kakarote.examine.entity.BO.ExamineBO;
import com.kakarote.examine.entity.BO.ExaminePreviewBO;
import com.kakarote.examine.entity.BO.ExamineUserBO;
import com.kakarote.examine.entity.PO.*;
import com.kakarote.examine.entity.VO.*;
import com.kakarote.examine.mapper.ExamineRecordMapper;
import com.kakarote.examine.service.*;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 审核记录表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-11-19
 */
@Slf4j
@Service
public class ExamineRecordServiceImpl extends BaseServiceImpl<ExamineRecordMapper, ExamineRecord> implements IExamineRecordService {

    @Autowired
    private IExamineService examineService;

    @Autowired
    private IExamineFlowService examineFlowService;

    @Autowired
    private IExamineRecordLogService examineRecordLogService;

    @Autowired
    private IExamineManagerUserService examineManagerUserService;

    @Autowired
    private IExamineRecordOptionalService recordOptionalService;

    @Autowired
    private CrmExamineService crmExamineService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private SalaryRecordService salaryRecordService;

    /**
     * 添加审批记录
     *
     * @param examineRecordSaveBO data
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExamineRecordReturnVO addExamineRecord(ExamineRecordSaveBO examineRecordSaveBO) {
        Examine examine;
        if (examineRecordSaveBO.getCategoryId() == null) {
            examine = examineService.queryExamineByLabel(examineRecordSaveBO.getLabel());
        }else {
            examine = examineService.getById(examineRecordSaveBO.getCategoryId());
        }
        /*
          没有可以审批流程时直接审核通过
         */
        if (examine == null) {
            return new ExamineRecordReturnVO(null, ExamineStatusEnum.PASS.getStatus(), null);
        }
        //编辑审批
        Integer recordId = examineRecordSaveBO.getRecordId();
        if (recordId != null) {
            Integer recheckType = examine.getRecheckType();
            //1 从第一层开始 2 从拒绝的层级开始
            if (Objects.equals(recheckType,2)) {
                List<ExamineRecordLog> recordLogList = examineRecordLogService.lambdaQuery().eq(ExamineRecordLog::getRecordId, recordId)
                        .eq(ExamineRecordLog::getExamineStatus, ExamineStatusEnum.INVALID.getStatus()).list();
                if(CollUtil.isEmpty(recordLogList)){
                    List<ExamineRecordLog> recordLogs = examineRecordLogService.lambdaQuery().eq(ExamineRecordLog::getRecordId, recordId)
                            .eq(ExamineRecordLog::getExamineStatus, ExamineStatusEnum.REJECT.getStatus()).list();
                    if (recordLogs != null && recordLogs.size() == 1){
                        recordLogList = recordLogs;
                    }
                }
                if (CollUtil.isNotEmpty(recordLogList)){
                    ExamineRecordLog recordLog = recordLogList.get(0);
                    recordLogList = examineRecordLogService.lambdaQuery().eq(ExamineRecordLog::getBatchId,recordLog.getBatchId()).list();
                    //1 依次审批 2 会签 3 或签
                    Integer type = recordLog.getType();
                    List<Integer> examineLogIds = recordLogList.stream().map(ExamineRecordLog::getRecordId).collect(Collectors.toList());
                    if (type == 1){
                        ExamineRecordLog recordLogOne = examineRecordLogService.lambdaQuery().eq(ExamineRecordLog::getRecordId, recordId)
                                .eq(ExamineRecordLog::getBatchId, recordLog.getBatchId())
                                .orderByAsc(ExamineRecordLog::getSort).last(" limit 1 ").one();
                        recordLogOne.setExamineStatus(ExamineStatusEnum.UNDERWAY.getStatus());
                        examineRecordLogService.updateById(recordLogOne);
                        if (recordLogList.size() > 1){
                            examineRecordLogService.lambdaUpdate().eq(ExamineRecordLog::getRecordId, recordId)
                                    .eq(ExamineRecordLog::getBatchId,recordLog.getBatchId())
                                    .gt(ExamineRecordLog::getSort,recordLogOne.getSort()).remove();
                        }
                        //重新发送通知
                        this.addMessage(examine, 1, examineRecordSaveBO.getTitle(), examineRecordSaveBO.getTypeId(), recordLog, UserUtil.getUserId(), false);
                        examineLogIds = ListUtil.toList(recordLog.getLogId());
                    }else {
                        examineRecordLogService.lambdaUpdate().set(ExamineRecordLog::getExamineStatus,ExamineStatusEnum.UNDERWAY.getStatus())
                                .eq(ExamineRecordLog::getRecordId, recordId)
                                .eq(ExamineRecordLog::getBatchId,recordLog.getBatchId()).update();
                        //重新发送通知
                        for (ExamineRecordLog examineRecordLog : recordLogList) {
                            this.addMessage(examine, 1, examineRecordSaveBO.getTitle(), examineRecordSaveBO.getTypeId(), examineRecordLog, UserUtil.getUserId(), false);
                        }
                    }

                    this.lambdaUpdate().set(ExamineRecord::getExamineStatus,ExamineStatusEnum.UNDERWAY.getStatus())
                            .eq(ExamineRecord::getRecordId, recordId).update();
                    return new ExamineRecordReturnVO(recordId, ExamineStatusEnum.UNDERWAY.getStatus(), examineLogIds);
                }
            }
            //默认返回初始层级
            recordOptionalService.lambdaUpdate().eq(ExamineRecordOptional::getRecordId, recordId).remove();
            examineRecordLogService.lambdaUpdate().eq(ExamineRecordLog::getRecordId, recordId).remove();
            this.lambdaUpdate().eq(ExamineRecord::getRecordId, recordId).remove();
        }
        //初次审批
        ExamineFlow examineFlow = examineFlowService.queryNextExamineFlow(examine.getExamineId(), null, examineRecordSaveBO.getDataMap());
        if (examineFlow == null) {
            return new ExamineRecordReturnVO(null, ExamineStatusEnum.PASS.getStatus(), null);
        }

        /*
        查询新增时审核人信息
         */
        ExamineUserBO examineUserBO = queryAddFlowUser(examineRecordSaveBO, examineFlow);
        if (examineUserBO.getRoleId() == null && examineUserBO.getUserList().size() == 0) {
            return new ExamineRecordReturnVO(null, ExamineStatusEnum.PASS.getStatus(), null);
        }

        if (examineUserBO.getExamineFlow() != null) {
            examineFlow = examineUserBO.getExamineFlow();
        }
        /*
          保存审核记录
         */
        UserInfo userInfo = UserUtil.getUser();
        ExamineRecord examineRecord = new ExamineRecord();
        examineRecord.setCreateUserId(userInfo.getUserId());
        examineRecord.setUpdateUserId(userInfo.getUserId());
        examineRecord.setCreateTime(new Date());
        examineRecord.setUpdateTime(new Date());
        examineRecord.setExamineStatus(ExamineStatusEnum.UNDERWAY.getStatus());
        examineRecord.setExamineId(examine.getExamineId());
        examineRecord.setFlowId(examineFlow.getFlowId());
        examineRecord.setLabel(examineRecordSaveBO.getLabel());
        examineRecord.setTypeId(examineRecordSaveBO.getTypeId());
        save(examineRecord);

        /*
          保持创建人log
         */
        ExamineRecordLog createRecordLog = new ExamineRecordLog();
        createRecordLog.setFlowId(examineFlow.getFlowId());
        createRecordLog.setCreateTime(examineRecord.getCreateTime());
        createRecordLog.setCreateUserId(userInfo.getUserId());
        createRecordLog.setExamineId(examine.getExamineId());
        createRecordLog.setExamineStatus(ExamineStatusEnum.CREATE.getStatus());
        createRecordLog.setRecordId(examineRecord.getRecordId());
        createRecordLog.setSort(0);
        createRecordLog.setBatchId(IdUtil.simpleUUID());
        examineRecordLogService.save(createRecordLog);

        /**
         * 保存自选成员的选择人员列表
         */
        List<ExamineGeneralBO> examineGeneralBoS = examineRecordSaveBO.getOptionalList();
        examineGeneralBoS.forEach(examineGeneralBo -> {
            recordOptionalService.saveRecordOptionalInfo(examineGeneralBo.getFlowId(), examineRecord.getRecordId(), examineGeneralBo.getUserList());
        });

        /*
         保存审批记录
         */
        List<Integer> examineLogIds = saveExamineRecordLog(examine, examineUserBO, examineFlow, examineRecord.getRecordId(), userInfo.getUserId(),examineRecordSaveBO);
        return new ExamineRecordReturnVO(examineRecord.getRecordId(), ExamineStatusEnum.UNDERWAY.getStatus(), examineLogIds);
    }

    /**
     * 进行审批，此方法采用异步同步的方式同步到其他模块
     * 开启分布式事务
     * 0待审核、1通过、2拒绝、3审核中 4:撤回 5 未提交 6 创建 7 已删除 8 作废
     *
     * @param examineBO data
     */
    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    public void auditExamine(ExamineBO examineBO) {
        UserInfo userInfo = UserUtil.getUser();
        examineBO.setExamineUserId(userInfo.getUserId());
        BaseUtil.getRedis().del(CrmCacheKey.CRM_BACKLOG_NUM_CACHE_KEY + userInfo.getUserId().toString());
        //查询当前审批日志
        ExamineRecordLog recordLog;
        if (ExamineStatusEnum.RECHECK.getStatus().equals(examineBO.getStatus())) {
            //撤回
            recordLog = examineRecordLogService.lambdaQuery()
                    .eq(ExamineRecordLog::getExamineStatus, ExamineStatusEnum.UNDERWAY.getStatus())
                    .eq(ExamineRecordLog::getRecordId, examineBO.getRecordId())
                    .eq(ExamineRecordLog::getCreateUserId, examineBO.getExamineUserId())
                    .orderByAsc(ExamineRecordLog::getCreateTime).last(" limit 1 ").one();
        }else {
            recordLog = examineRecordLogService.lambdaQuery()
                    .eq(ExamineRecordLog::getExamineStatus, ExamineStatusEnum.UNDERWAY.getStatus())
                    .eq(ExamineRecordLog::getRecordId, examineBO.getRecordId())
                    .eq(ExamineRecordLog::getExamineUserId, examineBO.getExamineUserId())
                    .orderByAsc(ExamineRecordLog::getCreateTime).last(" limit 1 ").one();
        }
        /*
          判断当前审核是否合法
         */

        if (recordLog == null) {
            return;
        }

        boolean isUser = recordLog.getCreateUserId().equals(userInfo.getUserId());
        boolean isRole = userInfo.getRoles().contains(recordLog.getExamineRoleId());
        boolean isRecheck = ExamineStatusEnum.RECHECK.getStatus().equals(examineBO.getStatus());
        if (isRecheck && (!isUser && !isRole)) {
            throw new CrmException(ExamineCodeEnum.EXAMINE_AUTH_ERROR);
        }
        recordLog.setRemarks(examineBO.getRemarks());
        //查询当前审批记录
        ExamineRecord examineRecord = getById(recordLog.getRecordId());
        ExamineModuleService moduleService = ApplicationContextHolder.getBean(ExamineEnum.parseModule(examineRecord.getLabel()).getServerName());
        /*
          校验审核状态是否合法
         */
        moduleService.checkStatus(examineRecord.getLabel(), examineRecord.getTypeId(), examineBO.getStatus(), examineRecord.getExamineStatus());

        /*
         当前审批通过则去寻找下一层
         */
        if (ExamineStatusEnum.PASS.getStatus().equals(examineBO.getStatus())) {

            //判断当前审批层同级是不是还有没走完的，会签，依次审批需要 1 依次审批 2 会签 3 或签
            if (recordLog.getType() == 1) {
                ExamineRecordLog examineRecordLog = examineRecordLogService.queryNextExamineRecordLog(recordLog.getBatchId(), recordLog.getSort(),recordLog.getLogId());
                /*
                  说明当前层级还有人需要审核，本条通过即可，将下层审批设为审核中,无需其他操作
                 */
                recordLog.setExamineStatus(examineBO.getStatus());
                examineRecordLogService.updateById(recordLog);
                if (examineRecordLog != null) {
                    examineRecordLog.setExamineStatus(ExamineStatusEnum.UNDERWAY.getStatus());
                    examineRecordLogService.updateById(examineRecordLog);
                    this.addMessage(examineService.getById(examineRecord.getExamineId()), examineRecordLog, examineRecord.getCreateUserId());
                    return;
                }
            } else if (recordLog.getType() == 2) {
                ExamineRecordLog examineRecordLog = examineRecordLogService.queryNextExamineRecordLog(recordLog.getBatchId(), null,recordLog.getLogId());
                /*
                 说明当前层级还有人未审核通过，本条通过即可,无需其他操作
                 */
                recordLog.setExamineStatus(examineBO.getStatus());
                examineRecordLogService.updateById(recordLog);
                if (examineRecordLog != null) {
                    return;
                }
            } else {
                examineRecordLogService.lambdaUpdate().set(ExamineRecordLog::getExamineStatus, examineBO.getStatus())
                        .eq(ExamineRecordLog::getBatchId, recordLog.getBatchId()).update();
            }
            /*
             获取自定义字段列表，用于条件审核
             */
            Map<String, Object> conditionMap = moduleService.getConditionMap(examineRecord.getLabel(), examineRecord.getTypeId(), examineRecord.getRecordId());
            ExamineFlow examineFlow = examineFlowService.queryNextExamineFlow(examineRecord.getExamineId(), recordLog.getFlowId(), conditionMap);
            if (examineFlow == null) {
                /*
                  已经没有下一层，审核通过
                 */
                updateCheckStatus(examineRecord, recordLog, ExamineStatusEnum.valueOf(examineBO.getStatus()));
            } else {
                /*
                 找下一层的审核人
                 */
                ExamineUserBO examineUserBO = queryFlowUser(examineFlow, examineRecord.getCreateUserId(), examineRecord.getRecordId());
                if (examineUserBO.getExamineFlow() != null) {
                    examineFlow = examineUserBO.getExamineFlow();
                }
                examineFlow.setExamineId(examineRecord.getExamineId());
                saveExamineRecordLog(examineUserBO, examineFlow, examineRecord.getRecordId(), examineRecord.getCreateUserId());
            }
        } else {
            updateCheckStatus(examineRecord, recordLog, ExamineStatusEnum.valueOf(examineBO.getStatus()));
        }

    }

    /**
     * 修改审核状态
     *
     * @param examineRecord    审核记录
     * @param examineRecordLog 当层审核记录
     * @param statusEnum       状态枚举
     */
    private void updateCheckStatus(ExamineRecord examineRecord, ExamineRecordLog examineRecordLog, ExamineStatusEnum statusEnum) {

        /*
         将其他正在进行中的当前审核设为废弃
         */
        examineRecordLogService.lambdaUpdate()
                .set(ExamineRecordLog::getExamineStatus, ExamineStatusEnum.INVALID.getStatus())
                .eq(ExamineRecordLog::getBatchId, examineRecordLog.getBatchId())
                .ne(ExamineRecordLog::getLogId, examineRecordLog.getLogId())
                .in(ExamineRecordLog::getExamineStatus, ExamineStatusEnum.UNDERWAY.getStatus(), ExamineStatusEnum.AWAIT.getStatus())
                .update();
        /*
         修改审核记录状态
         */
        examineRecord.setExamineStatus(statusEnum.getStatus());
        updateById(examineRecord);
        examineRecordLog.setExamineStatus(statusEnum.getStatus());
        examineRecordLog.setUpdateTime(new Date());
        examineRecordLogService.updateById(examineRecordLog);
        /*
         修改关联模块审核状态
         */
        ExamineModuleService moduleService = ApplicationContextHolder.getBean(ExamineEnum.parseModule(examineRecord.getLabel()).getServerName());
        moduleService.updateCheckStatus(examineRecord.getLabel(), examineRecord.getTypeId(), statusEnum.getStatus());

        Examine examine = examineService.getById(examineRecord.getExamineId());
        if (ExamineStatusEnum.PASS.equals(statusEnum)) {
            this.addMessage(examine, 2, examineRecordLog, examineRecord.getCreateUserId());
        } else if (ExamineStatusEnum.REJECT.equals(statusEnum)) {
            this.addMessage(examine, 3, examineRecordLog, examineRecord.getCreateUserId());
        }

    }


    private List<Integer> saveExamineRecordLog(ExamineUserBO examineUserBO, ExamineFlow examineFlow, Integer recordId, Long currentUserId){
        Examine examine = examineService.getById(examineFlow.getExamineId());
        return saveExamineRecordLog(examine,examineUserBO,examineFlow,recordId,currentUserId,null);
    }

    private List<Integer> saveExamineRecordLog(Examine examine,ExamineUserBO examineUserBO, ExamineFlow examineFlow, Integer recordId, Long currentUserId,ExamineRecordSaveBO examineRecordSaveBO) {
        Date date = new Date();
        /*
          通过角色审批
         */
        if (examineUserBO.getRoleId() != null && CollUtil.isEmpty(examineUserBO.getUserList())) {
            ExamineRecordLog examineRecordLog = new ExamineRecordLog();
            examineRecordLog.setType(examineUserBO.getType());
            examineRecordLog.setCreateTime(date);
            examineRecordLog.setUpdateTime(date);
            examineRecordLog.setCreateUserId(currentUserId);
            examineRecordLog.setExamineId(examineFlow.getExamineId());
            examineRecordLog.setExamineRoleId(examineUserBO.getRoleId());
            examineRecordLog.setExamineUserId(0L);
            examineRecordLog.setExamineStatus(ExamineStatusEnum.UNDERWAY.getStatus());
            examineRecordLog.setFlowId(examineFlow.getFlowId());
            examineRecordLog.setRecordId(recordId);
            examineRecordLog.setSort(0);
            examineRecordLog.setBatchId(IdUtil.simpleUUID());
            examineRecordLogService.save(examineRecordLog);
            List<Long> longList = adminService.queryUserIdByRoleId(examineUserBO.getRoleId()).getData();
            for (Long userId : longList) {
                BaseUtil.getRedis().del(CrmCacheKey.CRM_BACKLOG_NUM_CACHE_KEY + userId.toString());
            }
            return ListUtil.toList(examineRecordLog.getLogId());
        } else {
            /*
            通过员工审批
            */
            List<ExamineRecordLog> examineRecordLogList = new ArrayList<>();
            int i = 0;
            String batchId = IdUtil.simpleUUID();
            for (Long userId : examineUserBO.getUserList()) {
                ExamineRecordLog examineRecordLog = new ExamineRecordLog();
                examineRecordLog.setType(examineUserBO.getType());
                examineRecordLog.setCreateTime(date);
                examineRecordLog.setUpdateTime(date);
                examineRecordLog.setCreateUserId(currentUserId);
                examineRecordLog.setExamineId(examineFlow.getExamineId());
                examineRecordLog.setExamineRoleId(0);
                /*
                   在这块判断审核人状态，是否有效等
                 */
                examineRecordLog.setExamineUserId(userId);
                if (examineUserBO.getType() == 1){
                    if (i == 0){
                        examineRecordLog.setExamineStatus(ExamineStatusEnum.UNDERWAY.getStatus());
                    }else {
                        examineRecordLog.setExamineStatus(ExamineStatusEnum.AWAIT.getStatus());
                    }
                }else {
                    examineRecordLog.setExamineStatus(ExamineStatusEnum.UNDERWAY.getStatus());
                }
                BaseUtil.getRedis().del(CrmCacheKey.CRM_BACKLOG_NUM_CACHE_KEY + userId.toString());
                examineRecordLog.setFlowId(examineFlow.getFlowId());
                examineRecordLog.setRecordId(recordId);
                examineRecordLog.setSort(i++);
                examineRecordLog.setBatchId(batchId);
                examineRecordLogService.save(examineRecordLog);
                examineRecordLogList.add(examineRecordLog);
            }
            String title = null;
            Integer typeId = null;
            boolean isUnusual = false;
            if (examineRecordSaveBO != null){
                title = examineRecordSaveBO.getTitle();
                typeId = examineRecordSaveBO.getTypeId();
            }else {
                isUnusual = true;
            }
            for (ExamineRecordLog examineRecordLog : examineRecordLogList) {
                if (ExamineStatusEnum.UNDERWAY.getStatus().equals(examineRecordLog.getExamineStatus())) {
                    this.addMessage(examine, 1, title, typeId, examineRecordLog, currentUserId, isUnusual);
                }
            }
            return examineRecordLogList.stream().map(ExamineRecordLog::getLogId).collect(Collectors.toList());
        }
    }


    /**
     * 发送审核通知
     *
     * @param examine
     * @param examineType 审核通知类型  1 待审核 2 通过 3 拒绝
     * @param examineLog
     * @param userId
     * @return void
     * @date 2020/12/21 17:18
     **/
    private void addMessage(Examine examine, Integer examineType, ExamineRecordLog examineLog, Long userId){
        this.addMessage(examine,examineType,null,null,examineLog,userId,false);
    }

    /**
    * 依次审批专用
    * */
    private void addMessage(Examine examine, ExamineRecordLog examineLog, Long userId){
        this.addMessage(examine,1,null,null,examineLog,userId,true);
    }

    private void addMessage(Examine examine, Integer examineType,String title,Integer typeId, ExamineRecordLog examineLog, Long userId ,boolean isUnusual) {
        if (examine != null) {
            if (ListUtil.toList(1, 2, 3, 5, 6, 7, 8, 9, 10, 11, 12).contains(examine.getLabel())) {
                if (examineType == 1 && !isUnusual){
                    //正常通过
                    this.addMessageForNewExamine(examine.getLabel(),examineType,title,typeId,examineLog,userId);
                }else if (examineType == 2 || examineType ==3 || isUnusual) {
                    ExamineMessageBO examineMessageBO = new ExamineMessageBO();
                    examineMessageBO.setCategoryType(examine.getLabel());
                    examineMessageBO.setExamineType(examineType);
                    com.kakarote.core.feign.examine.entity.ExamineRecordLog examineRecordLog =
                            new com.kakarote.core.feign.examine.entity.ExamineRecordLog();
                    BeanUtil.copyProperties(examineLog, examineRecordLog);
                    examineMessageBO.setExamineLog(examineRecordLog);
                    examineMessageBO.setOwnerUserId(userId);
                    crmExamineService.addMessageForNewExamine(examineMessageBO);
                }
            }else if (Objects.equals(4,examine.getLabel())){
                this.addHrmMessageForNewExamine(examineType,examineLog,userId);
            }else if (Objects.equals(0,examine.getLabel())){
                ExamineRecord examineRecord = this.getById(examineLog.getRecordId());
                if (examineRecord == null) {
                    return;
                }
                AdminMessageBO adminMessageBO = new AdminMessageBO();
                if (examineType == 1) {
                    adminMessageBO.setUserId(userId);
                    adminMessageBO.setTitle(examine.getExamineName());
                    adminMessageBO.setTypeId(examineRecord.getTypeId());
                    adminMessageBO.setMessageType(AdminMessageEnum.OA_EXAMINE_NOTICE.getType());
                    adminMessageBO.setIds(Collections.singletonList(examineLog.getExamineUserId()));

                } else if (examineType == 2 || examineType == 3) {
                    adminMessageBO.setUserId(examineLog.getExamineUserId());
                    adminMessageBO.setMessageType(examineType == 2 ? AdminMessageEnum.OA_EXAMINE_PASS.getType() : AdminMessageEnum.OA_EXAMINE_REJECT.getType());
                    adminMessageBO.setContent(examineLog.getRemarks());
                    adminMessageBO.setTitle(examine.getExamineName());
                    adminMessageBO.setTypeId(examineRecord.getTypeId());
                    adminMessageBO.setIds(Collections.singletonList(examineRecord.getCreateUserId()));
                }
                if (adminMessageBO.getIds().size() > 0) {
                    AdminMessageService messageService = ApplicationContextHolder.getBean(AdminMessageService.class);
                    messageService.sendMessage(adminMessageBO);
                }
            }
        }
    }

    /**
     * 人资审核发送通知
     * */
    private void addHrmMessageForNewExamine(Integer examineType,ExamineRecordLog examineLog,Long userId){
        ExamineRecord examineRecord = this.getById(examineLog.getRecordId());
        if (examineRecord == null) {
            return;
        }
        HrmSalaryMonthRecord salaryMonthRecord = salaryRecordService.querySalaryRecordById(examineRecord.getTypeId()).getData();
        if (salaryMonthRecord == null){
            return;
        }
        AdminMessageBO adminMessageBO = new AdminMessageBO();
        if(examineType == 1){

            adminMessageBO.setUserId(userId);
            adminMessageBO.setTitle(salaryMonthRecord.getYear() + "年" + salaryMonthRecord.getMonth()+"月");
            adminMessageBO.setTypeId(examineRecord.getTypeId());
            adminMessageBO.setMessageType(AdminMessageEnum.HRM_EMPLOYEE_SALARY_EXAMINE.getType());
            adminMessageBO.setIds(Collections.singletonList(examineLog.getExamineUserId()));
        }else if (examineType == 2 || examineType ==3) {
            adminMessageBO.setTypeId(examineRecord.getTypeId());
            adminMessageBO.setUserId(examineLog.getExamineUserId());
            adminMessageBO.setTitle(salaryMonthRecord.getYear() + "年" + salaryMonthRecord.getMonth()+"月");
            adminMessageBO.setContent(examineLog.getRemarks());
            adminMessageBO.setMessageType(examineType == 2 ? AdminMessageEnum.HRM_EMPLOYEE_SALARY_PASS.getType() : AdminMessageEnum.HRM_EMPLOYEE_SALARY_REJECT.getType());
            adminMessageBO.setIds(Collections.singletonList(examineRecord.getCreateUserId()));
        }
        if (adminMessageBO.getIds().size() > 0) {
            AdminMessageService messageService = ApplicationContextHolder.getBean(AdminMessageService.class);
            messageService.sendMessage(adminMessageBO);
        }
    }

    /**
     * 发送待审核通知 客户与进销存模块
     * */
    private void addMessageForNewExamine(Integer categoryType, Integer examineType,String title,Integer typeId, ExamineRecordLog examineLog, Long ownerUserId) {
        AdminMessageBO adminMessageBO = new AdminMessageBO();
        adminMessageBO.setUserId(ownerUserId);
        if (examineType == 1) {
            if (categoryType == 1) {
                adminMessageBO.setTitle(title);
                adminMessageBO.setTypeId(typeId);
                adminMessageBO.setMessageType(AdminMessageEnum.CRM_CONTRACT_EXAMINE.getType());
            } else if (categoryType == 2) {
                adminMessageBO.setTitle(title);
                adminMessageBO.setTypeId(typeId);
                adminMessageBO.setMessageType(AdminMessageEnum.CRM_RECEIVABLES_EXAMINE.getType());
            } else if (categoryType == 3) {
                adminMessageBO.setTitle(title);
                adminMessageBO.setTypeId(typeId);
                adminMessageBO.setMessageType(AdminMessageEnum.CRM_INVOICE_EXAMINE.getType());
            }else {
                this.addMessageByExamine(categoryType,title,typeId,adminMessageBO);
            }
            adminMessageBO.setIds(Collections.singletonList(examineLog.getExamineUserId()));
        }
        if (adminMessageBO.getIds() != null && adminMessageBO.getIds().size() > 0) {
            AdminMessageService messageService = ApplicationContextHolder.getBean(AdminMessageService.class);
            messageService.sendMessage(adminMessageBO);
        }

    }

    /**
     * 补充消息详情
     * @date 2020/9/7 15:22
     * @param categoryType
     * @param adminMessageBO
     * @return void
     **/
    private void addMessageByExamine(Integer categoryType, String title,Integer typeId,AdminMessageBO adminMessageBO){
        switch (categoryType) {
            case 5: {
                //采购订单
                adminMessageBO.setTitle(title);
                adminMessageBO.setTypeId(typeId);
                adminMessageBO.setMessageType(AdminMessageEnum.JXC_PURCHASE_EXAMINE.getType());
                break;
            }
            case 6: {
                //采购退货单
                adminMessageBO.setTitle(title);
                adminMessageBO.setTypeId(typeId);
                adminMessageBO.setMessageType(AdminMessageEnum.JXC_RETREAT_EXAMINE.getType());
                break;
            }
            case 7: {
                //销售订单
                adminMessageBO.setTitle(title);
                adminMessageBO.setTypeId(typeId);
                adminMessageBO.setMessageType(AdminMessageEnum.JXC_SALE_EXAMINE.getType());
                break;
            }
            case 8: {
                //销售退货单
                adminMessageBO.setTitle(title);
                adminMessageBO.setTypeId(typeId);
                adminMessageBO.setMessageType(AdminMessageEnum.JXC_SALE_RETURN_EXAMINE.getType());
                break;
            }
            case 9: {
                //付款单
                adminMessageBO.setTitle(title);
                adminMessageBO.setTypeId(typeId);
                adminMessageBO.setMessageType(AdminMessageEnum.JXC_PAYMENT_EXAMINE.getType());
                break;
            }
            case 10: {
                //回款单
                adminMessageBO.setTitle(title);
                adminMessageBO.setTypeId(typeId);
                adminMessageBO.setMessageType(AdminMessageEnum.JXC_COLLECTION_EXAMINE.getType());
                break;
            }
            case 11: {
                //盘点
                adminMessageBO.setTitle(title);
                adminMessageBO.setTypeId(typeId);
                adminMessageBO.setMessageType(AdminMessageEnum.JXC_INVENTORY_EXAMINE.getType());
                break;
            }
            case 12: {
                //调拨
                adminMessageBO.setTitle(title);
                adminMessageBO.setTypeId(typeId);
                adminMessageBO.setMessageType(AdminMessageEnum.JXC_ALLOCATION_EXAMINE.getType());
                break;
            }
            default:
                break;
        }
    }



    /**
     * @param examineRecordSaveBO data
     * @return 审核人或者审核角色
     */
    private ExamineUserBO queryAddFlowUser(ExamineRecordSaveBO examineRecordSaveBO, ExamineFlow examineFlow) {
        ExamineTypeEnum examineTypeEnum = ExamineTypeEnum.valueOf(examineFlow.getExamineType());

        /*
          找不到用户管理员审批
         */
        if (ExamineTypeEnum.MANAGER.equals(examineTypeEnum)) {
            List<Long> longList = examineManagerUserService.queryExamineUser(examineFlow.getExamineId());
            return new ExamineUserBO().setExamineFlow(examineFlow).setUserList(longList);
        }

        ExamineTypeService examineTypeService = ApplicationContextHolder.getBean(examineTypeEnum.getServerName());

        Long userId = TypeUtils.castToLong(examineRecordSaveBO.getDataMap().get(ExamineConst.CREATE_USER_ID));
        ExamineUserBO examineUserBO = examineTypeService.queryFlowUser(userId, null, examineFlow);

        /*
          第一次提交的审批人自选直接从data里面获取
         */
        if (ExamineTypeEnum.OPTIONAL.equals(examineTypeEnum)) {
            List<ExamineGeneralBO> examineGeneralBoS = examineRecordSaveBO.getOptionalList();
            List<Long> userList = new ArrayList<>();
            for (ExamineGeneralBO examineGeneralBO : examineGeneralBoS) {
                if (examineFlow.getFlowId().equals(examineGeneralBO.getFlowId())) {
                    userList = examineGeneralBO.getUserList();
                    break;
                }
            }
            userList = examineService.handleUserList(userList,examineFlow.getExamineId());
            examineUserBO.setUserList(userList);
        }
        return examineUserBO;
    }

    public ExamineUserBO queryFlowUser(ExamineFlow examineFlow, Long createUserId, Integer recordId) {
        ExamineTypeEnum examineTypeEnum = ExamineTypeEnum.valueOf(examineFlow.getExamineType());
        /*
          找不到用户管理员审批
         */
        if (ExamineTypeEnum.MANAGER.equals(examineTypeEnum)) {
            List<Long> longList = examineManagerUserService.queryExamineUser(examineFlow.getExamineId());
            return new ExamineUserBO().setExamineFlow(examineFlow).setUserList(longList);
        }
        ExamineTypeService examineTypeService = ApplicationContextHolder.getBean(examineTypeEnum.getServerName());

        return examineTypeService.queryFlowUser(createUserId, recordId, examineFlow);
    }


    @Override
    public ExamineRecordVO queryExamineRecordList(Integer recordId, Long ownerUserId) {
        ExamineRecordVO examineRecordVO = new ExamineRecordVO();
        ExamineRecord examineRecord = getById(recordId);
        if (examineRecord == null){
            return examineRecordVO;
        }
        Integer label = examineRecord.getLabel();
        examineRecordVO.setLabel(label);
        //当前审批人
        Long auditUserId = UserUtil.getUserId();
        //判断是否有撤回权限
        boolean isValidUser = auditUserId.equals(examineRecord.getCreateUserId());
        boolean isNormalStatus = (examineRecord.getExamineStatus().equals(ExamineStatusEnum.AWAIT.getStatus()) || examineRecord.getExamineStatus().equals(ExamineStatusEnum.UNDERWAY.getStatus()));
        if (isValidUser && isNormalStatus) {
            examineRecordVO.setIsRecheck(1);
        } else {
            examineRecordVO.setIsRecheck(0);
        }

        ExaminePreviewBO examinePreviewBO = new ExaminePreviewBO();
        examinePreviewBO.setLabel(label);
        Map<String, Object> dataMap = new HashMap<>(8);
        List<ExamineFlowConditionDataVO> conditionDataVoS = examineService.previewFiledName(examineRecord.getLabel(), null, examineRecord.getExamineId());
        //判断有无条件审核，如果有获取相应数据
        if (conditionDataVoS != null) {
            List<String> fieldList = conditionDataVoS.stream().map(ExamineFlowConditionDataVO::getFieldName).collect(Collectors.toList());
            fieldList.removeIf(StrUtil::isEmpty);
            if (CollUtil.isNotEmpty(fieldList)) {
                ExamineModuleService moduleService = ApplicationContextHolder.getBean(ExamineEnum.parseModule(examineRecord.getLabel()).getServerName());
                dataMap = moduleService.getConditionMap(label, examineRecord.getTypeId(), recordId);
            }else {
                dataMap.put(ExamineConst.CREATE_USER_ID, examineRecord.getCreateUserId());
            }
        }
        examinePreviewBO.setDataMap(dataMap);
        examinePreviewBO.setStatus(1);
        examinePreviewBO.setRecordId(examineRecord.getRecordId());
        examinePreviewBO.setOwnerUserId(examineRecord.getCreateUserId());
        ExaminePreviewVO examinePreviewVO = examineService.previewExamineFlow(examinePreviewBO);
        List<ExamineFlowVO> examineFlowVoS = examinePreviewVO.getExamineFlowList();
        List<ExamineFlowDataVO> examineFlowDataVoS = new ArrayList<>();
        //发起人详细信息
        SimpleUser createUser = adminService.queryUserById(examineRecord.getCreateUserId()).getData();
        for (ExamineFlowVO examineFlowVO : examineFlowVoS) {
            ExamineFlowDataVO examineFlowDataVO = new ExamineFlowDataVO();
            BeanUtil.copyProperties(examineFlowVO, examineFlowDataVO, "userList");
            examineFlowDataVO.setExamineStatus(examineRecord.getExamineStatus());
            List<ExamineRecordLog> recordLogList = examineRecordLogService.lambdaQuery().eq(ExamineRecordLog::getRecordId, recordId)
                    .eq(ExamineRecordLog::getFlowId, examineFlowDataVO.getFlowId()).list();
            //判断审批流程是否已通过
            boolean isPass = true;
            //审核通过  记录状态
            Integer status = null;
            if (recordLogList.size() > 0) {
                for (ExamineRecordLog recordLog : recordLogList) {
                    boolean isNoPass = !ExamineStatusEnum.PASS.getStatus().equals(recordLog.getExamineStatus())
                            && !ExamineStatusEnum.CREATE.getStatus().equals(recordLog.getExamineStatus());
                    if (isNoPass) {
                        isPass = false;
                        break;
                    }
                }
                if (isPass) {
                    status = ExamineStatusEnum.PASS.getStatus();
                }
            }
            List<SimpleUser> userList = examineFlowVO.getUserList();
            List<Map<String, Object>> mapList = new ArrayList<>();
            Integer isEnd = null,breakStatus = null;
            if (CollUtil.isNotEmpty(userList)) {
                for (SimpleUser simpleUser : userList) {
                    Map<String, Object> map = new HashMap<>(6);
                    Long userId = simpleUser.getUserId();
                    map.put("userId", userId);
                    map.put("img", simpleUser.getImg());
                    map.put("realname", simpleUser.getRealname());
                    List<ExamineRecordLog> examineRecordLogList = examineRecordLogService.lambdaQuery()
                            .eq(ExamineRecordLog::getFlowId, examineFlowVO.getFlowId())
                            .eq(ExamineRecordLog::getRecordId, recordId)
                            .eq(ExamineRecordLog::getExamineUserId, userId).list();
                    if (CollUtil.isNotEmpty(examineRecordLogList)) {
                        Integer examineStatus = examineRecordLogList.get(0).getExamineStatus();
                        map.put("examineStatus", examineStatus);
                        map.put("examineTime", examineRecordLogList.get(0).getUpdateTime());
                        //跳过作废的
                        if (examineStatus == 8){
                            continue;
                        }
                        //审核进行中或已结束 记录状态
                        if (examineStatus != 0 && examineStatus != 1) {
                            isEnd = examineStatus;
                        }
                        //审核结束  记录状态
                        if (ListUtil.toList(2, 4, 5, 7).contains(examineStatus)) {
                            if (examineStatus == 4){
                                //使用发起人信息填充
                                map.put("userId", createUser.getUserId());
                                map.put("img", createUser.getImg());
                                map.put("realname", createUser.getRealname());
                            }
                            breakStatus = examineStatus;
                        }
                    } else {
                        if (status != null) {
                            map.put("examineStatus", status);
                        } else {
                            map.put("examineStatus", ExamineStatusEnum.AWAIT.getStatus());
                        }
                    }
                    mapList.add(map);
                    //审核结束 终止循环人员信息(对于或签拒绝时会产生无用审核人记录，终止循环可以去除多余信息)
                    if (breakStatus != null){
                        map.put("examineStatus", breakStatus);
                        break;
                    }
                }
            }
            //审核进行中或已结束 填充当前流程审核状态
            if (isEnd != null) {
                examineFlowDataVO.setExamineStatus(isEnd);
            } else {
                examineFlowDataVO.setExamineStatus(status != null ? status : ExamineStatusEnum.AWAIT.getStatus());
            }
            examineFlowDataVO.setUserList(mapList);
            if (status != null) {
                examineFlowDataVO.setExamineStatus(status);
            }
            examineFlowDataVoS.add(examineFlowDataVO);
            //审核结束 终止循环剩下的流程
            if (breakStatus != null){
                break;
            }
        }


        examineRecordVO.setCreateUser(createUser);
        //补充创建人节点
        ExamineFlowDataVO examineFlowDataVO = this.supplementExamineFlowDataVO(createUser,examineRecord.getCreateTime());
        examineFlowDataVoS.add(0, examineFlowDataVO);
        examineRecordVO.setExamineFlowList(examineFlowDataVoS);
        List<ExamineRecordLog> recordLogs = examineRecordLogService.lambdaQuery()
                .eq(ExamineRecordLog::getRecordId, recordId)
                .eq(ExamineRecordLog::getExamineStatus, ExamineStatusEnum.UNDERWAY.getStatus()).list();
        List<Long> examineUserIds = new ArrayList<>();
        if (CollUtil.isNotEmpty(recordLogs)) {
            examineUserIds = recordLogs.stream().map(ExamineRecordLog::getExamineUserId).collect(Collectors.toList());
        }
        //当前登录人是否有审核权限
        if (examineUserIds.contains(auditUserId)) {
            examineRecordVO.setIsCheck(1);
        } else {
            examineRecordVO.setIsCheck(0);
        }
        return examineRecordVO;
    }


    /**
     * 增加创建人节点
     *
     * @param simpleUser
     * @return com.kakarote.examine.entity.VO.ExamineFlowDataVO
     * @date 2020/12/22 10:33
     **/
    private ExamineFlowDataVO supplementExamineFlowDataVO(SimpleUser simpleUser,Date examineTime) {
        ExamineFlowDataVO examineFlowDataVO = new ExamineFlowDataVO();
        examineFlowDataVO.setFlowId(0);
        examineFlowDataVO.setExamineStatus(ExamineStatusEnum.CREATE.getStatus());
        Map<String, Object> map = new HashMap<>(6);
        Long userId = simpleUser.getUserId();
        map.put("userId", userId);
        map.put("img", simpleUser.getImg());
        map.put("realname", simpleUser.getRealname());
        map.put("examineStatus", ExamineStatusEnum.CREATE.getStatus());
        map.put("examineTime", examineTime);
        List<Map<String, Object>> userList = ListUtil.toList(map);
        examineFlowDataVO.setUserList(userList);
        return examineFlowDataVO;
    }

    @Override
    public ExamineRecordReturnVO queryExamineRecordInfo(Integer recordId) {
        ExamineRecordReturnVO examineRecordReturnVO = new ExamineRecordReturnVO();
        ExamineRecord examineRecord = this.getById(recordId);
        if (examineRecord == null) {
            log.error("审批记录查询错误，错误的记录id为{}", recordId);
            return examineRecordReturnVO;
        }
        examineRecordReturnVO.setRecordId(recordId);
        Integer examineStatus = examineRecord.getExamineStatus();
        examineRecordReturnVO.setExamineStatus(examineStatus);
        if (examineStatus != 1) {
            List<ExamineRecordLog> recordLogs = examineRecordLogService.lambdaQuery()
                    .eq(ExamineRecordLog::getRecordId, recordId)
                    .eq(ExamineRecordLog::getExamineStatus, examineStatus).list();
            if (CollUtil.isNotEmpty(recordLogs)) {
                List<Long> userIds = recordLogs.stream().map(ExamineRecordLog::getExamineUserId).collect(Collectors.toList());
                examineRecordReturnVO.setExamineUserIds(userIds);
            }
        }else {
            examineRecordReturnVO.setExamineUserIds(new ArrayList<>());
        }
        return examineRecordReturnVO;
    }

    @Override
    public Boolean deleteExamineRecord(Integer recordId) {
        recordOptionalService.lambdaUpdate().eq(ExamineRecordOptional::getRecordId, recordId).remove();
        examineRecordLogService.lambdaUpdate().eq(ExamineRecordLog::getRecordId, recordId).remove();
        return this.lambdaUpdate().eq(ExamineRecord::getRecordId, recordId).remove();
    }

    @Override
    public Boolean updateExamineRecord(Integer recordId, Integer examineStatus) {
        ExamineStatusEnum.valueOf(examineStatus);
        ExamineRecord examineRecord = this.getById(recordId);
        if (examineRecord == null){
            return false;
        }
        examineRecordLogService.lambdaUpdate()
                .set(ExamineRecordLog::getExamineStatus, examineStatus)
                .eq(ExamineRecordLog::getRecordId, recordId)
                .in(ExamineRecordLog::getExamineStatus, ExamineStatusEnum.UNDERWAY.getStatus(), ExamineStatusEnum.AWAIT.getStatus());
        /*
         修改审核记录状态
         */
        examineRecord.setExamineStatus(examineStatus);
        return updateById(examineRecord);
    }

    @Override
    public Boolean deleteExamineRecordAndLog(Integer label) {
        List<ExamineRecord> examineRecords = this.lambdaQuery().eq(ExamineRecord::getLabel, label).list();
        if(CollUtil.isNotEmpty(examineRecords)){
            List<Integer> recordIds = examineRecords.stream().map(ExamineRecord::getRecordId).collect(Collectors.toList());
            examineRecordLogService.lambdaUpdate().in(ExamineRecordLog::getRecordId,recordIds).remove();
            this.removeByIds(recordIds);
        }
        return true;
    }
}
