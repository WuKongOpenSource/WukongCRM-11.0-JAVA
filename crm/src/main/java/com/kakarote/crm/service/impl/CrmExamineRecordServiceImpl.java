package com.kakarote.crm.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.kakarote.core.common.Result;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.entity.AdminMessageBO;
import com.kakarote.core.feign.admin.entity.AdminMessageEnum;
import com.kakarote.core.feign.admin.service.AdminMessageService;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.feign.crm.entity.SimpleCrmInfo;
import com.kakarote.core.feign.examine.entity.ExamineConditionDataBO;
import com.kakarote.core.feign.examine.entity.ExamineMessageBO;
import com.kakarote.core.feign.examine.entity.ExamineRecordLog;
import com.kakarote.core.feign.hrm.entity.HrmSalaryMonthRecord;
import com.kakarote.core.feign.hrm.service.SalaryRecordService;
import com.kakarote.core.feign.jxc.entity.JxcExamine;
import com.kakarote.core.feign.jxc.entity.JxcState;
import com.kakarote.core.feign.jxc.service.JxcExamineService;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserCacheUtil;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.crm.common.CrmModel;
import com.kakarote.crm.common.ElasticUtil;
import com.kakarote.crm.constant.CrmCodeEnum;
import com.kakarote.crm.constant.CrmEnum;
import com.kakarote.crm.entity.BO.CrmExamineData;
import com.kakarote.crm.entity.PO.*;
import com.kakarote.crm.mapper.CrmExamineLogMapper;
import com.kakarote.crm.mapper.CrmExamineRecordMapper;
import com.kakarote.crm.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 审核记录表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-28
 */
@Service
public class CrmExamineRecordServiceImpl extends BaseServiceImpl<CrmExamineRecordMapper, CrmExamineRecord> implements ICrmExamineRecordService {

    @Autowired
    private ICrmExamineLogService examineLogService;

    @Autowired
    private ICrmExamineService examineService;

    @Autowired
    private ICrmExamineStepService examineStepService;

    @Autowired
    private ICrmContractService crmContractService;

    @Autowired
    private ICrmReceivablesService crmReceivablesService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private SalaryRecordService salaryRecordService;

    @Autowired
    private JxcExamineService jxcExamineService;

    /**
     * 保存审批记录
     *
     * @param type        类型 1合同，2回款
     * @param userId      审核用户ID
     * @param ownerUserId 服务人id
     * @param recordId    id
     * @param status      审核状态
     * @return data
     */
    @Override
    public CrmExamineData saveExamineRecord(Integer type, Long userId, Long ownerUserId, Integer recordId, Integer status) {
        CrmExamineData examineData = new CrmExamineData();
        List<Long> examineLogIdList = new ArrayList<>();
        //创建审核记录
        CrmExamineRecord examineRecord = new CrmExamineRecord();
        if (recordId != null) {
            examineRecord = getById(recordId);
            LambdaUpdateWrapper<CrmExamineLog> examineLogLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            examineLogLambdaUpdateWrapper.set(CrmExamineLog::getIsRecheck, 1);
            examineLogLambdaUpdateWrapper.eq(CrmExamineLog::getRecordId, recordId);
            examineLogService.update(examineLogLambdaUpdateWrapper);
        } else {
            examineRecord.setCreateTime(DateUtil.date());
            examineRecord.setCreateUser(UserUtil.getUserId());
        }
        //创建审核日志
        CrmExamineLog examineLog = new CrmExamineLog();
        examineLog.setCreateTime(DateUtil.date());
        examineLog.setCreateUser(UserUtil.getUserId());
        if (status != null) {
            examineLog.setExamineStatus(5);
            examineRecord.setExamineStatus(5);
        } else {
            examineLog.setExamineStatus(0);
            examineRecord.setExamineStatus(0);
        }
        examineLog.setOrderId(1);
        LambdaQueryWrapper<CrmExamine> crmExamineLambdaQueryWrapper = new LambdaQueryWrapper<>();
        crmExamineLambdaQueryWrapper.eq(CrmExamine::getCategoryType, type);
        crmExamineLambdaQueryWrapper.eq(CrmExamine::getStatus, 1);
        crmExamineLambdaQueryWrapper.orderByDesc(CrmExamine::getUpdateTime);
        crmExamineLambdaQueryWrapper.last(" limit 1");
        //根据type查询当前启用审批流程
        CrmExamine examine = examineService.getOne(crmExamineLambdaQueryWrapper);
        if (examine == null) {
            examineData.setStatus(0);
        } else {
            examineRecord.setExamineId(examine.getExamineId());

            //先判断该审批流程是否为固定审批
            if (examine.getExamineType() == 1) {
                //固定审批
                //先查询该审批流程的审批步骤的第一步
                LambdaQueryWrapper<CrmExamineStep> examineStepLambdaQueryWrapper = new LambdaQueryWrapper<>();
                examineStepLambdaQueryWrapper.eq(CrmExamineStep::getExamineId, examine.getExamineId());
                examineStepLambdaQueryWrapper.orderByAsc(CrmExamineStep::getStepNum);
                examineStepLambdaQueryWrapper.last(" limit 1");
                CrmExamineStep examineStep = examineStepService.getOne(examineStepLambdaQueryWrapper);
                if (examineStep == null){
                    throw new CrmException(CrmCodeEnum.NO_APPROVAL_STEP_CANNOT_BE_SAVED);
                }
                examineRecord.setExamineStepId(examineStep.getStepId());
                examineLog.setExamineStepId(examineStep.getStepId());
                if (recordId == null) {
                    save(examineRecord);
                } else {
                    updateById(examineRecord);
                }
                if (status != null) {
                    examineData.setStatus(1);
                    examineData.setRecordId(examineRecord.getRecordId());
                    return examineData;
                }
                if (examineStep.getStepType() == 2 || examineStep.getStepType() == 3) {
                    String[] userIds = examineStep.getCheckUserId().split(",");
                    for (String id : userIds) {
                        if (StrUtil.isNotEmpty(id)) {
                            examineLog.setLogId(null);
                            examineLog.setExamineUser(Long.valueOf(id));
                            examineLog.setRecordId(examineRecord.getRecordId());
                            examineLog.setIsRecheck(0);
                            examineLogService.save(examineLog);
                            examineLogIdList.add(examineLog.getLogId());
                        }
                    }
                } else if (examineStep.getStepType() == 1) {
                    //如果是负责人主管审批 获取主管ID
                    Long id = UserCacheUtil.getUserInfo(ownerUserId).getParentId();
                    if (id == null) {
                        id = 0L;
                    }
                    UserInfo parent = UserCacheUtil.getUserInfo(id);
                    if (parent == null || parent.getUserId() == null) {
                        examineLog.setExamineUser(UserUtil.getSuperUser());
                    } else {
                        examineLog.setExamineUser(parent.getUserId());
                    }
                    examineLog.setRecordId(examineRecord.getRecordId());
                    examineLog.setIsRecheck(0);
                    examineLogService.save(examineLog);
                    examineLogIdList.add(examineLog.getLogId());
                } else {
                    //如果是负责人主管审批 获取主管ID
                    Long id = UserCacheUtil.getUserInfo(ownerUserId).getParentId();
                    if (id == null) {
                        id = 0L;
                    }
                    Long pid = UserCacheUtil.getUserInfo(id).getParentId();
                    if (pid == null) {
                        pid = 0L;
                    }
                    //如果是负责人主管审批 获取主管的主管ID
                    UserInfo parent = UserCacheUtil.getUserInfo(pid);
                    if (parent == null || parent.getUserId() == null) {
                        examineLog.setExamineUser(UserUtil.getSuperUser());
                    } else {
                        examineLog.setExamineUser(parent.getUserId());
                    }
                    examineLog.setRecordId(examineRecord.getRecordId());
                    examineLog.setIsRecheck(0);
                    examineLogService.save(examineLog);
                    examineLogIdList.add(examineLog.getLogId());
                }

            } else {
                //固定审批改为授权审批需要把stepId置为null
                examineRecord.setExamineStepId(null);
                if (userId == null) {
                    return examineData.setStatus(2);
                }
                //授权审批
                examineLog.setExamineUser(userId);
                if (recordId == null) {
                    save(examineRecord);
                } else {
                    updateById(examineRecord);
                }
                examineLog.setRecordId(examineRecord.getRecordId());
                examineLogService.save(examineLog);
                examineLogIdList.add(examineLog.getLogId());

            }
            examineData.setExamineLogIdList(examineLogIdList);
            examineData.setStatus(1);
            examineData.setRecordId(examineRecord.getRecordId());
        }
        return examineData;
    }

    /**
     * 删除审批记录
     *
     * @param recordId id
     */
    @Override
    public void deleteExamine(Integer recordId) {
        LambdaQueryWrapper<CrmExamineLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CrmExamineLog::getRecordId, recordId);
        examineLogService.remove(queryWrapper);
        removeById(recordId);
    }

    /**
     * 更新合同回款金额
     *
     * @param id id
     */
    @Override
    public void updateContractMoney(Integer id) {
        CrmReceivables receivables = crmReceivablesService.getById(id);
        CrmContract contract = crmContractService.getById(receivables.getContractId());
        if (contract.getMoney().subtract(receivables.getMoney()).compareTo(BigDecimal.ZERO) < 0) {
            return;
        }
        BigDecimal unreceivedMoney = contract.getUnreceivedMoney();
        LambdaUpdateWrapper<CrmContract> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(CrmContract::getReceivedMoney, contract.getReceivedMoney().add(receivables.getMoney()));
        updateWrapper.set(CrmContract::getUnreceivedMoney, contract.getUnreceivedMoney().subtract(receivables.getMoney()));
        updateWrapper.eq(CrmContract::getContractId, contract.getContractId());
        updateWrapper.eq(CrmContract::getUnreceivedMoney, unreceivedMoney);
        crmContractService.update(updateWrapper);
        CrmContract newContract = crmContractService.getById(contract.getContractId());
        Map<String, Object> map = new HashMap<>();
        map.put("receivedMoney", newContract.getReceivedMoney());
        map.put("unreceivedMoney", newContract.getUnreceivedMoney());
        ElasticUtil.updateField(elasticsearchRestTemplate, map, newContract.getContractId(), CrmEnum.CONTRACT.getIndex());
    }

    /**
     * 更新合同未回款金额
     *
     * @param id id
     */
    @Override
    public void updateUnreceivedMoney(Integer id) {
        CrmReceivables receivables = crmReceivablesService.getById(id);
        CrmContract contract = crmContractService.getById(receivables.getContractId());
        receivables.setMoney(Optional.ofNullable(receivables.getMoney()).orElse(BigDecimal.ZERO));
        contract.setMoney(Optional.ofNullable(contract.getMoney()).orElse(BigDecimal.ZERO));
        if (contract.getMoney().subtract(Optional.ofNullable(receivables.getMoney()).orElse(BigDecimal.ZERO)).compareTo(BigDecimal.ZERO) < 0) {
            return;
        }
        BigDecimal unreceivedMoney = contract.getUnreceivedMoney();
        LambdaUpdateWrapper<CrmContract> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(CrmContract::getReceivedMoney, contract.getReceivedMoney().subtract(receivables.getMoney()));
        updateWrapper.set(CrmContract::getUnreceivedMoney, contract.getUnreceivedMoney().add(receivables.getMoney()));
        updateWrapper.eq(CrmContract::getContractId, contract.getContractId());
        updateWrapper.eq(CrmContract::getUnreceivedMoney, unreceivedMoney);
        crmContractService.update(updateWrapper);
        CrmContract newContract = crmContractService.getById(contract.getContractId());
        Map<String, Object> map = new HashMap<>();
        map.put("receivedMoney", newContract.getReceivedMoney());
        map.put("unreceivedMoney", newContract.getUnreceivedMoney());
        ElasticUtil.updateField(elasticsearchRestTemplate, map, newContract.getContractId(), CrmEnum.CONTRACT.getIndex());
    }

    @Override
    public JSONObject queryExamineRecordList(Integer recordId, Long ownerUserId) {
        CrmExamineLogMapper mapper = (CrmExamineLogMapper) examineLogService.getBaseMapper();
        JSONObject jsonObject = new JSONObject();
        JSONObject examineRecord = getBaseMapper().queryExamineRecordById(recordId);
        if (examineRecord == null){
            return jsonObject;
        }
        CrmExamine adminExamine = examineService.getById(examineRecord.getInteger("examineId"));
        List<JSONObject> list = new ArrayList<>();
        JSONObject rec = mapper.queryRecordAndId(recordId);
        //当前审批人
        Long auditUserId = UserUtil.getUserId();
        //jsonObject.put("isRecheck",0);
        //判断是否有撤回权限
        if ((auditUserId.equals(examineRecord.getLong("createUser")) || auditUserId.equals(UserUtil.getSuperUser()))
                && (examineRecord.getInteger("examineStatus") == 0 || examineRecord.getInteger("examineStatus") == 3)) {
            jsonObject.put("isRecheck", 1);
        } else {
            jsonObject.put("isRecheck", 0);
        }
        if (adminExamine.getExamineType() == 2) {
            JSONObject log = mapper.queryRecordByUserIdAndStatus(rec.getDate("examineTime"), rec.getLong("createUser")).get(0);
            rec.put("examinUser", log);
            list.add(rec);
            //授权审批
            List<JSONObject> logs = mapper.queryExamineLogAndUserByRecordId(recordId);
            logs.forEach(r -> {
                JSONObject l = mapper.queryExamineLogAndUserByLogId(r.getInteger("logId"));
                r.put("examinUser", l);
            });
            list.addAll(logs);
            //判断当前用户有没有权限审核
            JSONObject reco = mapper.queryExamineLog(auditUserId, recordId, null);
            if (reco != null) {
                jsonObject.put("isCheck", 1);
            } else {
                jsonObject.put("isCheck", 0);
            }
            jsonObject.put("examineType", 2);
            jsonObject.put("steps", list);
            return jsonObject;
        }
        jsonObject.put("examineType", 1);
        LambdaQueryWrapper<CrmExamineStep> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CrmExamineStep::getExamineId, adminExamine.getExamineId())
                .orderByAsc(CrmExamineStep::getStepNum);
        //固定审批
        List<Map<String, Object>> steps = examineStepService.listMaps(wrapper);
        //上一审核步骤
        Long lsatuUserId = null;
        for (Map<String, Object> step : steps) {
            List<JSONObject> logs;
            if (Objects.equals(1, step.get("stepType"))) {
                //负责人主管
                logs = mapper.queryUserByRecordIdAndStepIdAndStatus(recordId, (Long) step.get("stepId"));
                if (logs.size() == 1) {
                    if (logs.get(0).get("userId") == null) {
                        logs = null;
                    }

                }
                //已经创建审核日志
                if (logs != null && logs.size() != 0) {
                    for (JSONObject record : logs) {
                        step.put("examineStatus", record.getInteger("examineStatus"));
                    }
                    step.put("userList", logs);
                } else {
                    step.put("examineStatus", 0);
                    //还未创建审核日志
                    //查询负责人主管
                    List<JSONObject> r = mapper.queryUserByUserId(ownerUserId);
                    if (r.size() == 1) {
                        if (r.get(0).get("userId") == null) {
                            r = null;
                        }

                    }
                    if (r == null || r.size() == 0) {
                        r = mapper.queryUserByUserIdAnd(UserUtil.getSuperUser());
                    }
                    step.put("userList", r);
                }
            } else if (Objects.equals(2, step.get("stepType")) || Objects.equals(3, step.get("stepType"))) {
                //先判断是否已经审核过
                logs = mapper.queryUserByRecordIdAndStepIdAndStatus(recordId, (Long) step.get("stepId"));
                if (logs != null && logs.size() != 0) {
                    //已经创建审核日志
                    int status = 0;
                    if (Objects.equals(2, step.get("stepType"))) {
                        Optional<JSONObject> optional = logs.stream().filter(log -> log.getDate("examineTime") != null).sorted(Comparator.comparingLong(log -> log.getDate("examineTime").getTime())).findFirst();
                        if (optional.isPresent()) {
                            status = optional.get().getInteger("examineStatus");
                        }
                    }
                    if (Objects.equals(3, step.get("stepType"))) {
                        int i = 0;
                        for (JSONObject record : logs) {
                            if (record.getInteger("examineStatus") == 2) {
                                status = 2;
                            }
                            if (record.getInteger("examineStatus") == 1) {
                                i++;
                            }
                        }
                        if (i == logs.size()) {
                            status = 1;
                        }
                    }
                    step.put("examineStatus", status);
                    step.put("userList", logs);
                } else {
                    //该步骤还未审核
                    logs = new ArrayList<>();
                    String[] userIds = step.get("checkUserId").toString().split(",");
                    for (String userId : userIds) {
                        if (StrUtil.isNotEmpty(userId)) {
                            JSONObject user = mapper.queryUserByUserIdAnd(Long.valueOf(userId)).get(0);
                            if (user != null) {
                                logs.add(user);
                            }
                        }
                    }
                    step.put("examineStatus", 0);
                    step.put("userList", logs);
                }
            } else {
                //主管的主管
                logs = mapper.queryUserByRecordIdAndStepIdAndStatus(recordId, (Long) step.get("stepId"));
                if (logs.size() == 1) {
                    if (logs.get(0).getInteger("userId") == null) {
                        logs = null;
                    }

                }
                //已经创建审核日志
                if (logs != null && logs.size() != 0) {
                    for (JSONObject record : logs) {
                        step.put("examineStatus", record.getInteger("examineStatus"));
                    }
                    step.put("userList", logs);
                } else {
                    step.put("examineStatus", 0);
                    UserInfo adminUser = adminService.getUserInfo(lsatuUserId).getData();
                    if (adminUser != null && adminUser.getParentId() != null) {
                        logs = mapper.queryUserByUserIdAnd(adminUser.getParentId());
                    } else {
                        logs = mapper.queryUserByUserIdAnd(lsatuUserId);
                    }
                    //查询负责人主管的主管
                    step.put("userList", logs);
                }
            }
            if (logs != null && logs.size() > 0) {
                lsatuUserId = logs.get(0).getLong("userId");
            }
        }
        JSONObject reco = mapper.queryExamineLog(auditUserId, recordId, examineRecord.getLong("examineStepId"));
        //判断当前用户有没有权限审核
        if (reco != null && (examineRecord.getInteger("examineStatus") == 3 || examineRecord.getInteger("examineStatus") == 0)) {
            jsonObject.put("isCheck", 1);
        } else {
            jsonObject.put("isCheck", 0);
        }
        List<JSONObject> logs = mapper.queryRecordByUserIdAndStatus(rec.getDate("examineTime"), rec.getLong("createUser"));
        rec.fluentPut("userList", logs).fluentPut("stepType", 5);
        list.add(rec);
        list.addAll(steps.stream().map(JSONObject::new).collect(Collectors.toList()));
        jsonObject.put("steps", list);
        return jsonObject;
    }

    @Override
    public List<JSONObject> queryExamineLogList(Integer recordId, String ownerUserId) {
        CrmExamineRecord record = getById(recordId);
        //根据审核记录id查询审核记录
        Integer type = examineService.getById(record.getExamineId()).getCategoryType();
        JSONObject jsonObject;

        if (type == 1) {
            jsonObject = getBaseMapper().queryInfoByRecordId(recordId, "wk_crm_contract");
        } else if (type == 2) {
            jsonObject = getBaseMapper().queryInfoByRecordId(recordId, "wk_crm_receivables");
        } else if (type == 3) {
            jsonObject = getBaseMapper().queryInfoByRecordId(recordId, "wk_crm_invoice");
        } else if (type == 4 || type == 5 || type == 6 || type == 7 || type == 8 || type == 9 || type == 10 || type == 11 || type == 12) {
            jsonObject = new JSONObject();
            jsonObject.put("ownerUserId", Long.valueOf(ownerUserId));
            jsonObject.put("createTime", record.getCreateTime());
            jsonObject.put("examineStatus", record.getExamineStatus());
            jsonObject.put("checkStatus", record.getExamineStatus());
        } else {
            throw new CrmException(SystemCodeEnum.SYSTEM_NO_VALID);
        }
        if (type == 4){
            jsonObject.put("ownerUserId", record.getCreateUser());
            jsonObject.put("createTime", record.getCreateTime());
            jsonObject.put("examineStatus", record.getExamineStatus());
            jsonObject.put("checkStatus", record.getExamineStatus());
            jsonObject.put("realname", UserCacheUtil.getUserName(record.getCreateUser()));
        }else {
            jsonObject.put("realname", UserCacheUtil.getUserName(jsonObject.getLong("ownerUserId")));
        }
        CrmExamineLogMapper mapper = (CrmExamineLogMapper) examineLogService.getBaseMapper();
        List<JSONObject> logs = mapper.queryExamineLogByRecordIdByStep(recordId);
        List<JSONObject> recordList = new ArrayList<>();
        if (ObjectUtil.isNotEmpty(jsonObject.getInteger("checkStatus"))) {
            if (jsonObject.getInteger("checkStatus") != 5) {
                recordList.add(new JSONObject().fluentPut("examine_status", 6).fluentPut("examine_time", jsonObject.getDate("createTime")).fluentPut("orderId", 0).fluentPut("realname", jsonObject.getString("realname")).fluentPut("remarks", "").fluentPut("user_id", jsonObject.getLong("ownerUserId")));
            } else {
                recordList.add(new JSONObject().fluentPut("examine_status", 5).fluentPut("examine_time", jsonObject.getDate("createTime")).fluentPut("orderId", 0).fluentPut("realname", jsonObject.getString("realname")).fluentPut("remarks", "").fluentPut("user_id", jsonObject.getLong("ownerUserId")));
            }
        } else {
            recordList.add(new JSONObject().fluentPut("examine_status", record.getExamineStatus()).fluentPut("examine_time", jsonObject.getDate("createTime")).fluentPut("orderId", 0).fluentPut("realname", jsonObject.getString("realname")).fluentPut("remarks", "").fluentPut("user_id", jsonObject.getLong("ownerUserId")));
        }

        recordList.addAll(logs);
        return recordList;
    }

    @Override
    public void auditExamine(Integer recordId, Integer status, String remarks, Integer id, Long nextUserId) {
        //当前审批人
        Long auditUserId = UserUtil.getUserId();
        CrmExamineLogMapper mapper = (CrmExamineLogMapper) examineLogService.getBaseMapper();
        //根据审核记录id查询审核记录
        CrmExamineRecord examineRecord = getById(recordId);
        if (examineRecord.getExamineStatus() == 4) {
            throw new CrmException(CrmCodeEnum.CRM_EXAMINE_RECHECK_ERROR);
        }
        if (status == 4) {
            if (!examineRecord.getCreateUser().equals(auditUserId) && !auditUserId.equals(UserUtil.getSuperUser())) {
                throw new CrmException(CrmCodeEnum.CRM_EXAMINE_AUTH_ERROR);
            }
        } else {
            //【判断当前审批人是否有审批权限
            JSONObject reco = mapper.queryExamineLog(auditUserId, recordId, examineRecord.getExamineStepId());
            if (reco == null) {
                throw new CrmException(CrmCodeEnum.CRM_EXAMINE_AUTH_ERROR);
            }
        }
        examineRecord.setExamineStatus(status);
        //查询审批流程
        CrmExamine examine = examineService.getById(examineRecord.getExamineId());
        Long ownerUserId = getOwnerUserId(examine.getCategoryType(), id);
        //查询当前审批步骤
        CrmExamineStep examineStep = examineStepService.getById(examineRecord.getExamineStepId());
        //查询当前审核日志
        CrmExamineLog nowadayExamineLog;
        if (examine.getExamineType() == 1) {
            nowadayExamineLog = mapper.queryNowadayExamineLogByRecordIdAndStepId(examineRecord.getRecordId(), examineRecord.getExamineStepId(), auditUserId);
        } else {
            nowadayExamineLog = mapper.queryNowadayExamineLogByRecordIdAndStatus(examineRecord.getRecordId(), auditUserId);
        }

        //审核日志 添加审核人
        if (nowadayExamineLog != null) {
            nowadayExamineLog.setExamineTime(DateUtil.date());
            nowadayExamineLog.setRemarks(remarks);
        }

        if (status == 2) {
            //判断审核拒绝
            nowadayExamineLog.setExamineStatus(status);
            if (examineStep != null && examineStep.getStepType() == 2) {
                Integer count = mapper.queryCountByStepId(recordId, examineStep.getStepId());
                if (count == 0) {
                    examineRecord.setExamineStatus(status);
                }
            }
            updateCheckStatus(examine.getCategoryType(), status, id);
        } else if (status == 4) {
            //先查询该审批流程的审批步骤的第一步
            LambdaQueryWrapper<CrmExamineStep> examineStepLambdaQueryWrapper = new LambdaQueryWrapper<>();
            examineStepLambdaQueryWrapper.eq(CrmExamineStep::getExamineId, examine.getExamineId());
            examineStepLambdaQueryWrapper.orderByAsc(CrmExamineStep::getStepNum);
            examineStepLambdaQueryWrapper.last("limit 1");
            CrmExamineStep oneExamineStep = examineStepService.getOne(examineStepLambdaQueryWrapper);
            //判断审核撤回
            CrmExamineLog examineLog = new CrmExamineLog();
            examineLog.setLogId(null);
            examineLog.setExamineUser(auditUserId);
            examineLog.setCreateTime(DateUtil.date());
            examineLog.setCreateUser(auditUserId);
            examineLog.setExamineStatus(status);
            examineLog.setExamineTime(new Date());
            if (examine.getExamineType() == 1) {
                examineRecord.setExamineStepId(oneExamineStep.getStepId());
                examineLog.setExamineStepId(examineStep.getStepId());
                examineLog.setOrderId(examineStep.getStepNum());
            } else {
                CrmExamineLog e = examineLogService.lambdaQuery()
                        .select(CrmExamineLog::getOrderId)
                        .eq(CrmExamineLog::getRecordId, recordId)
                        .eq(CrmExamineLog::getIsRecheck, 0)
                        .ne(CrmExamineLog::getExamineStatus, 0)
                        .orderByDesc(CrmExamineLog::getOrderId)
                        .last(" limit 1").one();
                if (e == null) {
                    e = new CrmExamineLog();
                    e.setOrderId(1);
                }
                examineLog.setOrderId(e.getOrderId());
            }
            examineLog.setRecordId(examineRecord.getRecordId());
            examineLog.setRemarks(remarks);
            examineLogService.save(examineLog);
            examineLogService.lambdaUpdate().set(CrmExamineLog::getIsRecheck, 1).eq(CrmExamineLog::getRecordId, recordId).update();
            updateCheckStatus(examine.getCategoryType(), 4, id);
        } else {
            //审核通过
            nowadayExamineLog.setExamineStatus(status);
            //判断该审批流程类型
            if (examine.getExamineType() == 1) {
                //固定审批

                //查询下一个审批步骤
                CrmExamineStep nextExamineStep = mapper.queryExamineStepByNextExamineIdOrderByStepId(examine.getExamineId(), examineRecord.getExamineStepId());

                boolean flag = true;
                //判断是否是并签
                if (examineStep.getStepType() == 3) {
                    //查询当前并签是否都完成
                    //根据审核记录ID，审核步骤ID，查询审核日志
                    //当前并签人员
                    examineLogService.updateById(nowadayExamineLog);
                    String[] userIds = examineStep.getCheckUserId().split(",");
                    for (String userId : userIds) {
                        if (StrUtil.isNotEmpty(userId)) {
                            CrmExamineLog examineLog = mapper.queryNowadayExamineLogByRecordIdAndStepId(examineRecord.getRecordId(), examineRecord.getExamineStepId(), Long.valueOf(userId));
                            if (examineLog.getExamineStatus() == 0) {
                                //并签未走完
                                flag = false;
                                break;
                            }

                        }
                    }
                    //并签未完成
                    if (!flag) {
                        examineRecord.setExamineStatus(3);
                        updateCheckStatus(examine.getCategoryType(), 3, id);
                    }
                }
                if (flag) {
                    //判断是否有下一步流程三大队
                    if (nextExamineStep != null) {
                        //有下一步流程
                        examineRecord.setExamineStatus(3);
                        examineRecord.setExamineStepId(nextExamineStep.getStepId());

                        CrmExamineLog examineLog = new CrmExamineLog();
                        examineLog.setOrderId(nextExamineStep.getStepNum());
                        if (nextExamineStep.getStepType() == 2 || nextExamineStep.getStepType() == 3) {
                            //并签或者或签
                            String[] userIds = nextExamineStep.getCheckUserId().split(",");
                            for (String uid : userIds) {
                                if (StrUtil.isNotEmpty(uid)) {
                                    examineLog.setLogId(null);
                                    examineLog.setExamineUser(Long.valueOf(uid));
                                    examineLog.setCreateTime(DateUtil.date());
                                    examineLog.setCreateUser(UserUtil.getUserId());
                                    examineLog.setExamineStatus(0);
                                    examineLog.setIsRecheck(0);
                                    examineLog.setExamineStepId(nextExamineStep.getStepId());
                                    examineLog.setRecordId(examineRecord.getRecordId());
                                    examineLogService.save(examineLog);
                                    addMessage(examine.getCategoryType(), 1, examineLog, ownerUserId);
                                }
                            }
                        } else if (nextExamineStep.getStepType() == 1) {
                            List<JSONObject> r = mapper.queryUserByUserId(ownerUserId);

                            examineLog.setLogId(null);
                            if (r.size() == 0 || r.get(0).getLong("user_id") == null) {
                                examineLog.setExamineUser(UserUtil.getSuperUser());
                            } else {
                                examineLog.setExamineUser(r.get(0).getLong("user_id"));
                            }
                            examineLog.setExamineStatus(0);
                            examineLog.setCreateTime(DateUtil.date());
                            examineLog.setCreateUser(UserUtil.getUserId());
                            examineLog.setIsRecheck(0);
                            examineLog.setExamineStepId(nextExamineStep.getStepId());
                            examineLog.setRecordId(examineRecord.getRecordId());
                            examineLogService.save(examineLog);
                            addMessage(examine.getCategoryType(), 1, examineLog, ownerUserId);
                        } else {
                            examineLog.setLogId(null);
                            UserInfo adminUser = adminService.getUserInfo(nowadayExamineLog.getExamineUser()).getData();
                            if (adminUser != null && adminUser.getParentId() != null) {
                                examineLog.setExamineUser(adminUser.getParentId());
                            } else {
                                examineLog.setExamineUser(nowadayExamineLog.getExamineUser());
                            }

                            examineLog.setExamineStatus(0);
                            examineLog.setCreateTime(DateUtil.date());
                            examineLog.setCreateUser(UserUtil.getUserId());
                            examineLog.setExamineStepId(nextExamineStep.getStepId());
                            examineLog.setRecordId(examineRecord.getRecordId());
                            examineLog.setIsRecheck(0);
                            examineLogService.save(examineLog);
                            addMessage(examine.getCategoryType(), 1, examineLog, ownerUserId);
                        }
                        updateCheckStatus(examine.getCategoryType(), 3, id);
                    } else {
                        updateCheckStatus(examine.getCategoryType(), 1, id);
                    }
                }
            } else {
                //授权审批
                if (nextUserId != null) {
                    //有下一审批人
                    examineRecord.setExamineStatus(3);
                    CrmExamineLog examineLog = new CrmExamineLog();
                    examineLog.setCreateTime(DateUtil.date());
                    examineLog.setCreateUser(UserUtil.getUserId());
                    examineLog.setExamineUser(nextUserId);
                    examineLog.setExamineStatus(0);
                    examineLog.setIsRecheck(0);
                    examineLog.setRecordId(examineRecord.getRecordId());
                    examineLog.setOrderId(nowadayExamineLog.getOrderId() + 1);
                    examineLogService.save(examineLog);
                    addMessage(examine.getCategoryType(), 1, examineLog, ownerUserId);
                    updateCheckStatus(examine.getCategoryType(), 3, id);
                } else {
                    updateCheckStatus(examine.getCategoryType(), 1, id);
                }
            }
        }
        if (status != 4) {
            examineLogService.updateById(nowadayExamineLog);
        }
        examineRecord.setRemarks(remarks);
        if (examineRecord.getExamineStatus() == 1) {
            addMessage(examine.getCategoryType(), 2, examineRecord, auditUserId);
        } else if (examineRecord.getExamineStatus() == 2) {
            addMessage(examine.getCategoryType(), 3, examineRecord, auditUserId);
        }
        examineRecord.setRemarks(null);
        updateById(examineRecord);
    }

    /**
     * @param categoryType categoryType
     * @param examineType  1 待审核 2 通过 3 拒绝
     */
    @Override
    public void addMessage(Integer categoryType, Integer examineType, Object examineObj, Long ownerUserId) {
        AdminMessageBO adminMessageBO = new AdminMessageBO();
        adminMessageBO.setUserId(ownerUserId);
        if (examineType == 1) {
            if (examineObj instanceof CrmExamineLog) {
                CrmExamineLog examineLog = (CrmExamineLog) examineObj;
                if (categoryType == 1) {
                    CrmContract one = ApplicationContextHolder.getBean(ICrmContractService.class)
                            .lambdaQuery().eq(CrmContract::getExamineRecordId, examineLog.getRecordId()).last(" limit 1").one();
                    if (one == null) {
                        return;
                    }
                    adminMessageBO.setTitle(one.getName());
                    adminMessageBO.setTypeId(one.getContractId());
                    adminMessageBO.setMessageType(AdminMessageEnum.CRM_CONTRACT_EXAMINE.getType());
                } else if (categoryType == 2) {
                    CrmReceivables one = ApplicationContextHolder.getBean(ICrmReceivablesService.class)
                            .lambdaQuery().eq(CrmReceivables::getExamineRecordId, examineLog.getRecordId()).last(" limit 1").one();
                    if (one == null) {
                        return;
                    }
                    adminMessageBO.setTitle(one.getNumber());
                    adminMessageBO.setTypeId(one.getReceivablesId());
                    adminMessageBO.setMessageType(AdminMessageEnum.CRM_RECEIVABLES_EXAMINE.getType());
                } else if (categoryType == 3) {
                    CrmInvoice one = ApplicationContextHolder.getBean(ICrmInvoiceService.class)
                            .lambdaQuery().eq(CrmInvoice::getExamineRecordId, examineLog.getRecordId()).last(" limit 1").one();
                    if (one == null) {
                        return;
                    }
                    adminMessageBO.setTitle(one.getInvoiceApplyNumber());
                    adminMessageBO.setTypeId(one.getInvoiceId());
                    adminMessageBO.setMessageType(AdminMessageEnum.CRM_INVOICE_EXAMINE.getType());
                }else if (categoryType == 4) {
                    return;
                }else {
                    JxcExamine jxcExamine = new JxcExamine();
                    jxcExamine.setCategoryType(categoryType);
                    jxcExamine.setExamineType(examineType);
                    jxcExamine.setExamineObj((JSONObject) JSONObject.toJSON(examineLog));
                    jxcExamine.setOwnerUserId(ownerUserId);
                    jxcExamineService.examineMessage(jxcExamine);
                    return;
                }
                adminMessageBO.setIds(Collections.singletonList(examineLog.getExamineUser()));
            }
        } else if (examineType == 2 || examineType == 3) {
            if (examineObj instanceof CrmExamineRecord) {
                CrmExamineRecord examineRecord = (CrmExamineRecord) examineObj;
                adminMessageBO.setContent(examineRecord.getRemarks());
                if (categoryType == 1) {
                    CrmContract one = ApplicationContextHolder.getBean(ICrmContractService.class)
                            .lambdaQuery().eq(CrmContract::getExamineRecordId, examineRecord.getRecordId()).last(" limit 1").one();
                    if (one == null) {
                        return;
                    }
                    adminMessageBO.setTitle(one.getName());
                    adminMessageBO.setTypeId(one.getContractId());
                    adminMessageBO.setMessageType(examineType == 2 ? AdminMessageEnum.CRM_CONTRACT_PASS.getType() : AdminMessageEnum.CRM_CONTRACT_REJECT.getType());
                    adminMessageBO.setIds(Collections.singletonList(one.getOwnerUserId()));
                } else if (categoryType == 2) {
                    CrmReceivables one = ApplicationContextHolder.getBean(ICrmReceivablesService.class)
                            .lambdaQuery().eq(CrmReceivables::getExamineRecordId, examineRecord.getRecordId()).last(" limit 1").one();
                    if (one == null) {
                        return;
                    }
                    adminMessageBO.setTitle(one.getNumber());
                    adminMessageBO.setTypeId(one.getReceivablesId());
                    adminMessageBO.setMessageType(examineType == 2 ? AdminMessageEnum.CRM_RECEIVABLES_PASS.getType() : AdminMessageEnum.CRM_RECEIVABLES_REJECT.getType());
                    adminMessageBO.setIds(Collections.singletonList(one.getOwnerUserId()));
                } else if (categoryType == 3) {
                    CrmInvoice one = ApplicationContextHolder.getBean(ICrmInvoiceService.class)
                            .lambdaQuery().eq(CrmInvoice::getExamineRecordId, examineRecord.getRecordId()).last(" limit 1").one();
                    if (one == null) {
                        return;
                    }
                    adminMessageBO.setTitle(one.getInvoiceApplyNumber());
                    adminMessageBO.setTypeId(one.getInvoiceId());
                    adminMessageBO.setMessageType(examineType == 2 ? AdminMessageEnum.CRM_INVOICE_PASS.getType() : AdminMessageEnum.CRM_INVOICE_REJECT.getType());
                    adminMessageBO.setIds(Collections.singletonList(one.getOwnerUserId()));
                }else if (categoryType == 4) {
                    return;
                }else {
                    JxcExamine jxcExamine = new JxcExamine();
                    jxcExamine.setCategoryType(categoryType);
                    jxcExamine.setExamineType(examineType);
                    jxcExamine.setExamineObj((JSONObject) JSONObject.toJSON(examineRecord));
                    jxcExamine.setOwnerUserId(ownerUserId);
                    jxcExamineService.examineMessage(jxcExamine);
                    return;
                }
            }
        }
        if (adminMessageBO.getIds() != null && adminMessageBO.getIds().size() > 0) {
            AdminMessageService messageService = ApplicationContextHolder.getBean(AdminMessageService.class);
            messageService.sendMessage(adminMessageBO);
        }
    }


    /**
     * @param categoryType 类型
     * @return id
     */
    private Long getOwnerUserId(Integer categoryType, Integer id) {
        Long ownerUserId;
        if (categoryType == 1) {
            ownerUserId = ApplicationContextHolder.getBean(ICrmContractService.class).getById(id).getOwnerUserId();
        } else if (categoryType == 2) {
            ownerUserId = ApplicationContextHolder.getBean(ICrmReceivablesService.class).getById(id).getOwnerUserId();
        } else if (categoryType == 3) {
            //发票功能
            ownerUserId = ApplicationContextHolder.getBean(ICrmInvoiceService.class).getById(id).getOwnerUserId();
        } else if (categoryType == 4) {
            //薪资功能
            Result<HrmSalaryMonthRecord> hrmSalaryMonthRecordResult = salaryRecordService.querySalaryRecordById(id);
            ownerUserId = hrmSalaryMonthRecordResult.getData().getCreateUserId();
        } else if (categoryType == 5 || categoryType == 6 || categoryType == 7 || categoryType == 8 || categoryType == 9 || categoryType == 10 || categoryType == 11 || categoryType == 12) {
            //jxc
            Result<JxcState> result = jxcExamineService.queryJxcById(categoryType, id);
            ownerUserId = Long.valueOf(result.getData().getOwnerUserId());
        } else {
            ownerUserId = 1L;
        }
        return ownerUserId;
    }

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    /**
     * todo 修改审核状态
     * 0待审核、1通过、2拒绝、3审核中 4:撤回 5 未提交 6 创建 7 已删除 8 作废
     *
     * @param categoryType 类型
     * @param status       状态
     * @param id           id
     */
    private void updateCheckStatus(Integer categoryType, Integer status, Integer id) {
        //审核是否能够撤回的逻辑
        if (categoryType == 1) {
            CrmContract contract = ApplicationContextHolder.getBean(ICrmContractService.class).getById(id);
            if (status == 4) {
                if (contract.getCheckStatus() == 1) {
                    throw new CrmException(CrmCodeEnum.CRM_EXAMINE_RECHECK_PASS_ERROR);
                }
            } else if (status == 1) {
                CrmCustomer customer = ApplicationContextHolder.getBean(ICrmCustomerService.class).getById(contract.getCustomerId());
                customer.setDealStatus(1);
                Date dealTime = contract.getOrderDate() != null ? contract.getOrderDate() : new Date();
                customer.setDealTime(dealTime);
                ApplicationContextHolder.getBean(ICrmCustomerService.class).updateById(customer);
                Map<String, Object> map = new HashMap<>();
                map.put("dealTime", DateUtil.formatDateTime(dealTime));
                map.put("dealStatus", 1);
                ElasticUtil.updateField(elasticsearchRestTemplate, map, customer.getCustomerId(), CrmEnum.CUSTOMER.getIndex());
            }
            contract.setCheckStatus(status);
            ApplicationContextHolder.getBean(ICrmContractService.class).updateById(contract);
            ElasticUtil.updateField(elasticsearchRestTemplate, "checkStatus", status, Collections.singletonList(contract.getContractId()), CrmEnum.CONTRACT.getIndex());
        } else if (categoryType == 2) {
            CrmReceivables receivables = ApplicationContextHolder.getBean(ICrmReceivablesService.class).getById(id);
            if (status == 4) {
                if (receivables.getCheckStatus() == 1) {
                    throw new CrmException(CrmCodeEnum.CRM_EXAMINE_RECHECK_PASS_ERROR);
                }
            } else if (status == 1) {
                updateContractMoney(id);
            }
            receivables.setCheckStatus(status);
            //回款
            ApplicationContextHolder.getBean(ICrmReceivablesService.class).updateById(receivables);
            ElasticUtil.updateField(elasticsearchRestTemplate, "checkStatus", status, Collections.singletonList(receivables.getReceivablesId()), CrmEnum.RECEIVABLES.getIndex());
        } else if (categoryType == 3) {
            CrmInvoice invoice = ApplicationContextHolder.getBean(ICrmInvoiceService.class).getById(id);
            if (status == 4) {
                if (invoice.getCheckStatus() == 1) {
                    throw new CrmException(CrmCodeEnum.CRM_EXAMINE_RECHECK_PASS_ERROR);
                }
            }
            invoice.setCheckStatus(status);
            ApplicationContextHolder.getBean(ICrmInvoiceService.class).updateById(invoice);
        } else if (categoryType == 4) {
            Result<HrmSalaryMonthRecord> hrmSalaryMonthRecordResult = salaryRecordService.querySalaryRecordById(id);
            HrmSalaryMonthRecord salaryMonthRecord = hrmSalaryMonthRecordResult.getData();
            if (status == 4) {
                if (salaryMonthRecord.getCheckStatus() == 1) {
                    throw new CrmException(CrmCodeEnum.CRM_EXAMINE_RECHECK_PASS_ERROR);
                }
            }
            ExamineConditionDataBO examineConditionDataBO = new ExamineConditionDataBO();
            examineConditionDataBO.setTypeId(id);
            examineConditionDataBO.setCheckStatus(status);
            salaryRecordService.updateCheckStatus(id, status);
        } else if (categoryType == 5 || categoryType == 6 || categoryType == 7 || categoryType == 8 || categoryType == 9 || categoryType == 10 || categoryType == 11 || categoryType == 12) {
            Result<JxcState> result = jxcExamineService.queryJxcById(categoryType, id);
            JxcState jxcState = result.getData();
            if (status == 4) {
                if (jxcState.getState() == 3) {
                    throw new CrmException(CrmCodeEnum.CRM_EXAMINE_RECHECK_PASS_ERROR);
                }
            }
            jxcExamineService.examine(categoryType, status, id);
        }

    }


    @Override
    public void addMessageForNewExamine(ExamineMessageBO examineMessageBO) {
        this.addMessageForNewExamine(examineMessageBO.getCategoryType(),examineMessageBO.getExamineType(),
                examineMessageBO.getExamineLog(),examineMessageBO.getOwnerUserId());
    }

    @Override
    public void addMessageForNewExamine(Integer categoryType, Integer examineType, Object examineObj, Long ownerUserId) {
        AdminMessageBO adminMessageBO = new AdminMessageBO();
        if (examineType == 1) {
            if (examineObj instanceof ExamineRecordLog) {
                ExamineRecordLog examineLog = (ExamineRecordLog) examineObj;
                adminMessageBO.setUserId(ownerUserId);
                if (categoryType == 1) {
                    CrmContract one = ApplicationContextHolder.getBean(ICrmContractService.class)
                            .lambdaQuery().eq(CrmContract::getExamineRecordId, examineLog.getRecordId()).last(" limit 1").one();
                    if (one == null) {
                        return;
                    }
                    adminMessageBO.setTitle(one.getName());
                    adminMessageBO.setTypeId(one.getContractId());
                    adminMessageBO.setMessageType(AdminMessageEnum.CRM_CONTRACT_EXAMINE.getType());
                } else if (categoryType == 2) {
                    CrmReceivables one = ApplicationContextHolder.getBean(ICrmReceivablesService.class)
                            .lambdaQuery().eq(CrmReceivables::getExamineRecordId, examineLog.getRecordId()).last(" limit 1").one();
                    if (one == null) {
                        return;
                    }
                    adminMessageBO.setTitle(one.getNumber());
                    adminMessageBO.setTypeId(one.getReceivablesId());
                    adminMessageBO.setMessageType(AdminMessageEnum.CRM_RECEIVABLES_EXAMINE.getType());
                } else if (categoryType == 3) {
                    CrmInvoice one = ApplicationContextHolder.getBean(ICrmInvoiceService.class)
                            .lambdaQuery().eq(CrmInvoice::getExamineRecordId, examineLog.getRecordId()).last(" limit 1").one();
                    if (one == null) {
                        return;
                    }
                    adminMessageBO.setTitle(one.getInvoiceApplyNumber());
                    adminMessageBO.setTypeId(one.getInvoiceId());
                    adminMessageBO.setMessageType(AdminMessageEnum.CRM_INVOICE_EXAMINE.getType());
                }else if (categoryType == 4) {
                    return;
                }else {
                    JxcExamine jxcExamine = new JxcExamine();
                    jxcExamine.setCategoryType(categoryType);
                    jxcExamine.setExamineType(examineType);
                    jxcExamine.setExamineObj((JSONObject) JSONObject.toJSON(examineLog));
                    jxcExamine.setOwnerUserId(ownerUserId);
                    jxcExamineService.examineMessage(jxcExamine);
                    return;
                }
                adminMessageBO.setIds(Collections.singletonList(examineLog.getExamineUserId()));
            }
        } else if (examineType == 2 || examineType == 3) {
            if (examineObj instanceof ExamineRecordLog) {
                ExamineRecordLog examineRecord = (ExamineRecordLog) examineObj;
                adminMessageBO.setContent(examineRecord.getRemarks());
                adminMessageBO.setUserId(examineRecord.getExamineUserId());
                if (categoryType == 1) {
                    CrmContract one = ApplicationContextHolder.getBean(ICrmContractService.class)
                            .lambdaQuery().eq(CrmContract::getExamineRecordId, examineRecord.getRecordId()).last(" limit 1").one();
                    if (one == null) {
                        return;
                    }
                    adminMessageBO.setTitle(one.getName());
                    adminMessageBO.setTypeId(one.getContractId());
                    adminMessageBO.setMessageType(examineType == 2 ? AdminMessageEnum.CRM_CONTRACT_PASS.getType() : AdminMessageEnum.CRM_CONTRACT_REJECT.getType());
                    adminMessageBO.setIds(Collections.singletonList(one.getOwnerUserId()));
                } else if (categoryType == 2) {
                    CrmReceivables one = ApplicationContextHolder.getBean(ICrmReceivablesService.class)
                            .lambdaQuery().eq(CrmReceivables::getExamineRecordId, examineRecord.getRecordId()).last(" limit 1").one();
                    if (one == null) {
                        return;
                    }
                    adminMessageBO.setTitle(one.getNumber());
                    adminMessageBO.setTypeId(one.getReceivablesId());
                    adminMessageBO.setMessageType(examineType == 2 ? AdminMessageEnum.CRM_RECEIVABLES_PASS.getType() : AdminMessageEnum.CRM_RECEIVABLES_REJECT.getType());
                    adminMessageBO.setIds(Collections.singletonList(one.getOwnerUserId()));
                } else if (categoryType == 3) {
                    CrmInvoice one = ApplicationContextHolder.getBean(ICrmInvoiceService.class)
                            .lambdaQuery().eq(CrmInvoice::getExamineRecordId, examineRecord.getRecordId()).last(" limit 1").one();
                    if (one == null) {
                        return;
                    }
                    adminMessageBO.setTitle(one.getInvoiceApplyNumber());
                    adminMessageBO.setTypeId(one.getInvoiceId());
                    adminMessageBO.setMessageType(examineType == 2 ? AdminMessageEnum.CRM_INVOICE_PASS.getType() : AdminMessageEnum.CRM_INVOICE_REJECT.getType());
                    adminMessageBO.setIds(Collections.singletonList(one.getOwnerUserId()));
                }else if (categoryType == 4) {
                    return;
                }else {
                    JxcExamine jxcExamine = new JxcExamine();
                    jxcExamine.setCategoryType(categoryType);
                    jxcExamine.setExamineType(examineType);
                    jxcExamine.setExamineObj((JSONObject) JSONObject.toJSON(examineRecord));
                    jxcExamine.setOwnerUserId(ownerUserId);
                    jxcExamineService.examineMessage(jxcExamine);
                    return;
                }
            }
        }
        if (adminMessageBO.getIds() != null && adminMessageBO.getIds().size() > 0) {
            AdminMessageService messageService = ApplicationContextHolder.getBean(AdminMessageService.class);
            messageService.sendMessage(adminMessageBO);
        }

    }


    @Override
    public Map<String, Object> getDataMapForNewExamine(ExamineConditionDataBO examineConditionDataBO) {
        Map<String, Object> dataMap = new HashMap<>(8);
        Integer label = examineConditionDataBO.getLabel();
        Integer id = examineConditionDataBO.getTypeId();
        if (label == 1){
            CrmModel crmModel = crmContractService.queryById(id);
            List<String> fieldList = examineConditionDataBO.getFieldList();
            fieldList.forEach(fieldName -> dataMap.put(fieldName,crmModel.get(fieldName)));
            dataMap.put("createUserId",crmModel.get("createUserId"));
        }else if (label == 2){
            CrmModel crmModel = crmReceivablesService.queryById(id);
            List<String> fieldList = examineConditionDataBO.getFieldList();
            fieldList.forEach(fieldName -> dataMap.put(fieldName,crmModel.get(fieldName)));
            dataMap.put("createUserId",crmModel.get("createUserId"));
        }else if (label == 3){
            ICrmInvoiceService crmInvoiceService = ApplicationContextHolder.getBean(ICrmInvoiceService.class);
            LambdaQueryWrapper<CrmInvoice> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(CrmInvoice::getInvoiceId,id);
            Map<String, Object> crmModel = crmInvoiceService.getMap(lambdaQueryWrapper);
            List<String> fieldList = examineConditionDataBO.getFieldList();
            fieldList.forEach(fieldName -> dataMap.put(fieldName,crmModel.get(fieldName)));
            dataMap.put("createUserId",crmModel.get("createUserId"));
        }
        return dataMap;
    }

    @Override
    public Boolean updateCheckStatusByNewExamine(ExamineConditionDataBO examineConditionDataBO) {
        Integer categoryType = examineConditionDataBO.getLabel();
        Integer id = examineConditionDataBO.getTypeId();
        Integer status = examineConditionDataBO.getCheckStatus();
        //审核是否能够撤回的逻辑
        if (categoryType == 1) {
            CrmContract contract = ApplicationContextHolder.getBean(ICrmContractService.class).getById(id);
            if (status == 4) {
                if (contract.getCheckStatus() == 1) {
                    throw new CrmException(CrmCodeEnum.CRM_EXAMINE_RECHECK_PASS_ERROR);
                }
            } else if (status == 1) {
                CrmCustomer customer = ApplicationContextHolder.getBean(ICrmCustomerService.class).getById(contract.getCustomerId());
                customer.setDealStatus(1);
                Date dealTime = contract.getOrderDate() != null ? contract.getOrderDate() : new Date();
                customer.setDealTime(dealTime);
                ApplicationContextHolder.getBean(ICrmCustomerService.class).updateById(customer);
                Map<String, Object> map = new HashMap<>();
                map.put("dealTime", DateUtil.formatDateTime(dealTime));
                map.put("dealStatus", 1);
                ElasticUtil.updateField(elasticsearchRestTemplate, map, customer.getCustomerId(), CrmEnum.CUSTOMER.getIndex());
            }
            contract.setCheckStatus(status);
            ApplicationContextHolder.getBean(ICrmContractService.class).updateById(contract);
            ElasticUtil.updateField(elasticsearchRestTemplate, "checkStatus", status, Collections.singletonList(contract.getContractId()), CrmEnum.CONTRACT.getIndex());
        } else if (categoryType == 2) {
            CrmReceivables receivables = ApplicationContextHolder.getBean(ICrmReceivablesService.class).getById(id);
            if (status == 4) {
                if (receivables.getCheckStatus() == 1) {
                    throw new CrmException(CrmCodeEnum.CRM_EXAMINE_RECHECK_PASS_ERROR);
                }
            } else if (status == 1) {
                updateContractMoney(id);
            }
            receivables.setCheckStatus(status);
            //回款
            ApplicationContextHolder.getBean(ICrmReceivablesService.class).updateById(receivables);
            ElasticUtil.updateField(elasticsearchRestTemplate, "checkStatus", status, Collections.singletonList(receivables.getReceivablesId()), CrmEnum.RECEIVABLES.getIndex());
        } else if (categoryType == 3) {
            CrmInvoice invoice = ApplicationContextHolder.getBean(ICrmInvoiceService.class).getById(id);
            if (status == 4) {
                if (invoice.getCheckStatus() == 1) {
                    throw new CrmException(CrmCodeEnum.CRM_EXAMINE_RECHECK_PASS_ERROR);
                }
            }
            invoice.setCheckStatus(status);
            ApplicationContextHolder.getBean(ICrmInvoiceService.class).updateById(invoice);
        } else if (categoryType == 4) {
            Result<HrmSalaryMonthRecord> hrmSalaryMonthRecordResult = salaryRecordService.querySalaryRecordById(id);
            HrmSalaryMonthRecord salaryMonthRecord = hrmSalaryMonthRecordResult.getData();
            if (status == 4) {
                if (salaryMonthRecord.getCheckStatus() == 1) {
                    throw new CrmException(CrmCodeEnum.CRM_EXAMINE_RECHECK_PASS_ERROR);
                }
            }
            salaryRecordService.updateCheckStatus(id, status);
        } else if (categoryType == 5 || categoryType == 6 || categoryType == 7 || categoryType == 8 || categoryType == 9 || categoryType == 10 || categoryType == 11 || categoryType == 12) {
            Result<JxcState> result = jxcExamineService.queryJxcById(categoryType, id);
            JxcState jxcState = result.getData();
            if (status == 4) {
                if (jxcState.getState() == 3) {
                    throw new CrmException(CrmCodeEnum.CRM_EXAMINE_RECHECK_PASS_ERROR);
                }
            }
            jxcExamineService.examine(categoryType, status, id);
        }
        return true;
    }

    @Override
    public SimpleCrmInfo getCrmSimpleInfo(ExamineConditionDataBO examineConditionDataBO) {
        SimpleCrmInfo simpleCrmInfo = new SimpleCrmInfo();
        Integer label = examineConditionDataBO.getLabel();
        Integer id = examineConditionDataBO.getTypeId();
        if (label == 1){
            CrmContract crmContract = crmContractService.getById(id);
            if (crmContract == null){
                return null;
            }
            CrmCustomer crmCustomer = ApplicationContextHolder.getBean(ICrmCustomerService.class).getById(crmContract.getCustomerId());
            simpleCrmInfo.setCategory(crmContract.getName());
            simpleCrmInfo.setCategoryId(crmContract.getContractId());
            simpleCrmInfo.setOrderDate(crmContract.getOrderDate());
            simpleCrmInfo.setCategoryCiteId(crmCustomer.getCustomerId());
            simpleCrmInfo.setCategoryCiteName(crmCustomer.getCustomerName());
            simpleCrmInfo.setCreateUser(adminService.queryUserById(crmContract.getCreateUserId()).getData());
        }else if (label == 2){
            CrmReceivables crmReceivables = crmReceivablesService.getById(id);
            if (crmReceivables == null){
                return null;
            }
            CrmContract crmContract = crmContractService.getById(crmReceivables.getContractId());
            simpleCrmInfo.setCategory(crmReceivables.getNumber());
            simpleCrmInfo.setCategoryId(crmReceivables.getReceivablesId());
            simpleCrmInfo.setReturnTime(crmReceivables.getReturnTime());
            simpleCrmInfo.setCategoryCiteId(crmContract.getContractId());
            simpleCrmInfo.setCategoryCiteName(crmContract.getName());
            simpleCrmInfo.setCreateUser(adminService.queryUserById(crmReceivables.getCreateUserId()).getData());
        }else if (label == 3){
            CrmInvoice crmInvoice = ApplicationContextHolder.getBean(ICrmInvoiceService.class).getById(id);
            if (crmInvoice == null){
                return null;
            }
            CrmContract crmContract = crmContractService.getById(crmInvoice.getContractId());
            simpleCrmInfo.setCategory(crmInvoice.getInvoiceApplyNumber());
            simpleCrmInfo.setCategoryId(crmInvoice.getInvoiceId());
            simpleCrmInfo.setRealInvoiceDate(crmInvoice.getRealInvoiceDate());
            simpleCrmInfo.setCategoryCiteId(crmContract.getContractId());
            simpleCrmInfo.setCategoryCiteName(crmContract.getName());
            simpleCrmInfo.setCreateUser(adminService.queryUserById(crmInvoice.getCreateUserId()).getData());
        }
        return simpleCrmInfo;
    }
}
