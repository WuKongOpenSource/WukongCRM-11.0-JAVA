package com.kakarote.examine.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.TypeUtils;
import com.kakarote.core.common.Const;
import com.kakarote.core.common.Result;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.entity.SimpleDept;
import com.kakarote.core.feign.admin.entity.SimpleUser;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.feign.crm.entity.ExamineField;
import com.kakarote.core.feign.crm.entity.SimpleCrmInfo;
import com.kakarote.core.feign.crm.service.CrmExamineService;
import com.kakarote.core.feign.examine.entity.ExamineConditionDataBO;
import com.kakarote.core.feign.oa.OaService;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.TagUtil;
import com.kakarote.core.utils.UserCacheUtil;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.examine.constant.*;
import com.kakarote.examine.entity.BO.ExamineDataSaveBO;
import com.kakarote.examine.entity.BO.ExaminePageBO;
import com.kakarote.examine.entity.BO.ExaminePreviewBO;
import com.kakarote.examine.entity.BO.ExamineSaveBO;
import com.kakarote.examine.entity.PO.*;
import com.kakarote.examine.entity.VO.*;
import com.kakarote.examine.mapper.ExamineMapper;
import com.kakarote.examine.mapper.ExamineRecordLogMapper;
import com.kakarote.examine.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 审批表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-11-13
 */
@Service
public class ExamineServiceImpl extends BaseServiceImpl<ExamineMapper, Examine> implements IExamineService {

    @Autowired
    private IExamineFlowService examineFlowService;

    @Autowired
    private IExamineManagerUserService examineManagerUserService;

    @Autowired
    private IExamineConditionService examineConditionService;

    @Autowired
    private IExamineRecordOptionalService examineRecordOptionalService;

    @Autowired
    private IExamineRecordService examineRecordService;

    @Autowired
    private ExamineRecordLogMapper examineRecordLogMapper;

    @Autowired
    private AdminService adminService;

    @Autowired
    private OaService oaService;

    @Autowired
    private CrmExamineService crmExamineService;


    /**
     * 查询可供设置的自定义字段列表
     *
     * @param label      字段类型
     * @param categoryId 审批分类，只有OA审批需要
     * @return data
     */
    @Override
    public List<ExamineField> queryField(Integer label, Integer categoryId) {
        ExamineModuleService moduleService = ApplicationContextHolder.getBean(ExamineEnum.parseModule(label).getServerName());
        return moduleService.queryExamineField(label, categoryId);
    }

    /**
     * 查询审批列表
     *
     * @param examinePageBo 分页对象
     * @return data
     */
    @Override
    public BasePage<ExamineVO> queryList(ExaminePageBO examinePageBo) {
        UserInfo user = UserUtil.getUser();
        BasePage<Examine> basePage = this.getBaseMapper().selectPartExaminePage(examinePageBo.parse(),examinePageBo.getLabel(),UserUtil.isAdmin(),
                examinePageBo.getIsPart(),user.getUserId(),user.getDeptId());
        BasePage<ExamineVO> page = new BasePage<>(basePage.getCurrent(), basePage.getSize(), basePage.getTotal(), basePage.isSearchCount());
        List<ExamineVO> examineVoList = new ArrayList<>();
        for (Examine examine : basePage.getList()) {
            ExamineVO examineVO = new ExamineVO();
            BeanUtil.copyProperties(examine,examineVO);
            if (!examinePageBo.getIsPart()) {
                examineVO.setCreateUserName(UserCacheUtil.getUserName(examine.getCreateUserId()));
                examineVO.setUpdateUserName(UserCacheUtil.getUserName(examine.getUpdateUserId()));
                examineVO.setManagerList(examineManagerUserService.queryExamineUserByPage(examineVO.getExamineId()));
                //办公审批流可见范围
                if (StrUtil.isNotEmpty(examine.getUserIds())) {
                    Set<Long> userIds = TagUtil.toLongSet(examine.getUserIds());
                    if (CollUtil.isNotEmpty(userIds)) {
                        Result<List<SimpleUser>> listResult = adminService.queryUserByIds(userIds);
                        examineVO.setUserList(listResult.getData());
                    } else {
                        examineVO.setUserList(new ArrayList<>());
                    }
                }
                if (StrUtil.isNotEmpty(examine.getDeptIds())) {
                    Set<Integer> deptIds = TagUtil.toSet(examine.getDeptIds());
                    if (CollUtil.isNotEmpty(deptIds)) {
                        Result<List<SimpleDept>> listResult = adminService.queryDeptByIds(deptIds);
                        examineVO.setDeptList(listResult.getData());
                    } else {
                        examineVO.setDeptList(new ArrayList<>());
                    }
                }
            }
            examineVoList.add(examineVO);
        }
        page.setList(examineVoList);
        return page;
    }

    /**
     * 保存审批对象，审批对象不可修改，修改是新增一个再把原来的停用
     *
     * @param examineSaveBO data
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Examine addExamine(ExamineSaveBO examineSaveBO) {

        String batchId = IdUtil.simpleUUID();
        /*
          保存审批对象
         */
        Examine examine = saveExamine(examineSaveBO, batchId);
        Long examineId = examine.getExamineId();
        /*
          保存审批管理员对象
         */

        List<Long> longList = examineSaveBO.getManagerList();
        List<ExamineManagerUser> managerUserList = new ArrayList<>();
        for (int i = 0; i < longList.size(); i++) {
            ExamineManagerUser managerUser = new ExamineManagerUser();
            managerUser.setExamineId(examineId);
            managerUser.setSort(i + 1);
            managerUser.setUserId(longList.get(i));
            managerUserList.add(managerUser);
        }
        examineManagerUserService.saveBatch(managerUserList, Const.BATCH_SAVE_SIZE);

        /*
          保存审批流程数据
         */
        saveExamineFlow(examineSaveBO.getDataList(), examineId, batchId);

        return examine;
    }

    /**
     * 通过label查询可用审批流
     *
     * @param label 类型
     * @return data
     */
    @Override
    public Examine queryExamineByLabel(Integer label) {
        return lambdaQuery().eq(Examine::getLabel, label).eq(Examine::getStatus, ExamineStatusEnum.PASS.getStatus()).last(" limit 1").one();
    }

    /**
     * 保存审批数据
     *
     * @param examineSaveBO data
     * @return 审批ID
     */
    private Examine saveExamine(ExamineSaveBO examineSaveBO, String batchId) {
        Integer oldStatus = -1;
        if (examineSaveBO.getExamineId() != null) {
            /*
              如果是修改审批直接将原来停用
             */
            oldStatus = updateStatus(examineSaveBO.getExamineId(), 3);
        }
        String examineName = examineSaveBO.getExamineName();
        Integer count;
        if (examineSaveBO.getLabel() == 0) {
            count = lambdaQuery().eq(Examine::getExamineName, examineName).eq(Examine::getLabel,0).ne(Examine::getStatus, 3).count();
        }else {
            count = lambdaQuery().eq(Examine::getExamineName, examineName).ne(Examine::getLabel,0).ne(Examine::getStatus, 3).count();
        }
        if (count > 0) {
            throw new CrmException(ExamineCodeEnum.EXAMINE_NAME_NO_USER_ERROR);
        }
        Examine examine = new Examine();
        if (examineSaveBO.getLabel() == 0){
            examine.setStatus(oldStatus == 2 ? 2 : 1);
        }else {
            examine.setStatus(oldStatus == 1 ? 1 : 2);
        }
        examine.setExamineName(examineName);
        examine.setLabel(examineSaveBO.getLabel());
        examine.setExamineIcon(examineSaveBO.getExamineIcon());
        examine.setRecheckType(examineSaveBO.getRecheckType());
        examine.setRemarks(examineSaveBO.getRemarks());
        examine.setCreateTime(new Date());
        examine.setCreateUserId(UserUtil.getUserId());
        examine.setExamineId(examineSaveBO.getExamineId());
        examine.setBatchId(batchId);
        examine.setUpdateUserId(UserUtil.getUserId());
        examine.setUpdateTime(new Date());
        if (examineSaveBO.getUserList() != null) {
            List<Long> list = examineSaveBO.getUserList();
            examine.setUserIds(TagUtil.fromLongSet(list));
        }
        if (examineSaveBO.getDeptList() != null) {
            List<Integer> list = examineSaveBO.getDeptList();
            examine.setDeptIds(TagUtil.fromSet(list));
        }
        save(examine);
        if(examine.getLabel() == 0){
            if (examineSaveBO.getExamineId() != null) {
                oaService.updateFieldCategoryId(examine.getExamineId(), examineSaveBO.getExamineId());
            }else {
                oaService.saveDefaultField(examine.getExamineId());
            }
        }
        return examine;
    }

    /**
     * 保存审批流程数据
     */
    private void saveExamineFlow(List<ExamineDataSaveBO> dataList, Long examineId, String batchId) {
        int i = 1;
        for (ExamineDataSaveBO dataSaveBO : dataList) {
            ExamineTypeEnum examineTypeEnum = ExamineTypeEnum.valueOf(dataSaveBO.getExamineType());
            ExamineFlow IExamineFlow = new ExamineFlow();
            IExamineFlow.setExamineType(dataSaveBO.getExamineType());
            IExamineFlow.setExamineId(examineId);
            IExamineFlow.setName(dataSaveBO.getName());
            IExamineFlow.setConditionId(0);
            IExamineFlow.setCreateTime(new Date());
            IExamineFlow.setCreateUserId(UserUtil.getUserId());
            IExamineFlow.setExamineErrorHandling(dataSaveBO.getExamineErrorHandling());
            IExamineFlow.setSort(i++);
            IExamineFlow.setBatchId(batchId);
            examineFlowService.save(IExamineFlow);
            ExamineTypeService examineTypeService = ApplicationContextHolder.getBean(examineTypeEnum.getServerName());
            examineTypeService.saveExamineFlowData(dataSaveBO, IExamineFlow.getFlowId(), batchId);
        }
    }


    /**
     * 修改审批状态
     *
     * @param examineId 审批ID
     * @param status    1 正常 2 停用 3 删除
     */
    @Override
    public Integer updateStatus(Long examineId, Integer status) {
        if (!ObjectUtil.isAllNotEmpty(examineId, status)) {
            throw new CrmException(SystemCodeEnum.SYSTEM_NO_VALID);
        }
        if (!Arrays.asList(1, 2, 3).contains(status)) {
            return -1;
        }
        Examine examine = getById(examineId);
        if (examine == null) {
            return -1;
        }
        /*
          状态相同直接跳过
         */
        Integer oldStatus = examine.getStatus();
        if (oldStatus.equals(status)) {
            return oldStatus;
        }

        /*
          已删除审批不允许进行其他操作
         */
        if (Objects.equals(3, oldStatus)) {
            return oldStatus;
        }
        /*
          每个类型只允许一个启用的审批
         */
        if (Objects.equals(1, status)) {
            Integer label = examine.getLabel();
            if (label != 0) {
                Integer count = lambdaQuery().eq(Examine::getLabel, examine.getLabel()).eq(Examine::getStatus, 1).count();
                if (count > 0) {
                    throw new CrmException(ExamineCodeEnum.EXAMINE_START_ERROR);
                }
            }
        }

        examine.setStatus(status);
        examine.setUpdateUserId(UserUtil.getUserId());
        updateById(examine);
        return oldStatus;
    }

    /**
     * 查询指定类型的审批流程树
     *
     * @param label 类型
     */
    @Override
    public List<ExamineFlowVO> previewExamineFlow(Integer label) {
        Examine examine = lambdaQuery().eq(Examine::getLabel, label).eq(Examine::getStatus, 1).last(" limit 1").one();
        return getExamineFlowVOList(examine,null);
    }

    @Override
    public List<ExamineFlowVO> queryExamineFlow(Long examineId) {
        Examine examine = this.getById(examineId);
        return getExamineFlowVOList(examine,null);
    }

    @Override
    public List<ExamineFlowConditionDataVO> previewFiledName(Integer label,Integer recordId,Long examineId) {
        Examine examine;
        if (examineId == null) {
            if (recordId == null) {
                examine = lambdaQuery().eq(Examine::getLabel, label).eq(Examine::getStatus, 1).last(" limit 1").one();
            } else {
                ExamineRecord record = examineRecordService.getById(recordId);
                Long exId = Optional.ofNullable(record).orElse(new ExamineRecord()).getExamineId();
                examine = this.getById(exId);
            }
        }else {
            examine = this.getById(examineId);
        }
        if (examine == null){
            return null;
        }
        List<ExamineFlowVO> examineFlowVOList = getExamineFlowVOList(examine,null);
        List<ExamineFlowConditionDataVO> conditionDataVoS = new ArrayList<>();
        this.getAllFiledName(examineFlowVOList,conditionDataVoS);
        conditionDataVoS.removeIf(conditionDataVo -> ConditionTypeEnum.PERSONNEL.getType().equals(conditionDataVo.getConditionType()));
        return conditionDataVoS;
    }

    /**
     * 获取条件字段名称
     * @date 2020/12/16 11:12
     * @param examineFlowVOList
     * @param conditionDataVoS
     * @return void
     **/
    private void getAllFiledName(List<ExamineFlowVO> examineFlowVOList,List<ExamineFlowConditionDataVO> conditionDataVoS){
        for (ExamineFlowVO examineFlowVO : examineFlowVOList) {
            List<ExamineFlowConditionVO> conditionList = examineFlowVO.getConditionList();
            if (CollUtil.isNotEmpty(conditionList)) {
                for (ExamineFlowConditionVO examineFlowConditionVO : conditionList) {
                    List<ExamineFlowConditionDataVO> conditionDataList = examineFlowConditionVO.getConditionDataList();
                    if (CollUtil.isNotEmpty(conditionDataList)) {
                        for (ExamineFlowConditionDataVO examineFlowConditionDataVO : conditionDataList) {
                            examineFlowConditionDataVO.setValues(null);
                            if (!conditionDataVoS.contains(examineFlowConditionDataVO)){
                                conditionDataVoS.add(examineFlowConditionDataVO);
                            }
                        }
                    }
                    List<ExamineFlowVO> examineDataList = examineFlowConditionVO.getExamineDataList();
                    if (CollUtil.isNotEmpty(examineDataList)) {
                        this.getAllFiledName(examineDataList,conditionDataVoS);
                    }
                }
            }
        }
    }

    @Override
    public ExaminePreviewVO previewExamineFlow(ExaminePreviewBO examinePreviewBO) {
        ExaminePreviewVO examinePreviewVO = new ExaminePreviewVO();
        Examine examine;
        Integer recordId = examinePreviewBO.getRecordId();
        if (examinePreviewBO.getExamineId() == null) {
            if (recordId == null) {
                examine = lambdaQuery().eq(Examine::getLabel, examinePreviewBO.getLabel()).eq(Examine::getStatus, 1).last(" limit 1").one();
            } else {
                ExamineRecord record = examineRecordService.getById(recordId);
                Long examineId = Optional.ofNullable(record).orElse(new ExamineRecord()).getExamineId();
                examine = this.getById(examineId);
            }
        }else {
            //OA审批
            examine = this.getById(examinePreviewBO.getExamineId());
        }
        if (examine == null){
            return examinePreviewVO;
        }
        examinePreviewVO.setRemarks(examine.getRemarks());
        List<ExamineFlowVO> examineFlowVOList = getExamineFlowVOList(examine,examinePreviewBO.getOwnerUserId());
        Map<String, Object> dataMap = examinePreviewBO.getDataMap();
        List<ExamineFlowVO> examineFlowVoS = new ArrayList<>();
        this.getAllConformExamineFlow(examineFlowVOList,dataMap,examineFlowVoS);
        if (Objects.equals(examinePreviewBO.getStatus(),1)){
            this.supplementOptionalUserInfo(examineFlowVoS,examinePreviewBO.getRecordId(),examine.getExamineId());
        }
        examinePreviewVO.setExamineFlowList(examineFlowVoS);
        return examinePreviewVO;
    }


    /**
     * 预览审批流程时补充自选用户信息
     * @param examineFlowVoS
     * @param recordId
     * @param examineId
     * @return void
     **/
    private void supplementOptionalUserInfo(List<ExamineFlowVO> examineFlowVoS,Integer recordId,Long examineId){
        for (ExamineFlowVO examineFlowVO : examineFlowVoS) {
            if (ExamineTypeEnum.OPTIONAL.getType().equals(examineFlowVO.getExamineType())) {
                List<ExamineRecordOptional> optionalUsers = examineRecordOptionalService.lambdaQuery()
                        .eq(ExamineRecordOptional::getFlowId, examineFlowVO.getFlowId())
                        .eq(ExamineRecordOptional::getRecordId,recordId)
                        .orderByAsc(ExamineRecordOptional::getSort).list();
                if (CollUtil.isNotEmpty(optionalUsers)) {
                    List<Long> userIds = optionalUsers.stream().map(ExamineRecordOptional::getUserId).collect(Collectors.toList());
                    List<SimpleUser> simpleUsers = adminService.queryUserByIds(handleUserList(userIds,examineId)).getData();
                    examineFlowVO.setUserList(simpleUsers);
                }
            }
        }
    }

    @Override
    public List<Long> handleUserList(List<Long> userIds, Long examineId){
        List<Long> userList = new ArrayList<>();
        if (CollUtil.isNotEmpty(userIds)){
            AdminService adminService = ApplicationContextHolder.getBean(AdminService.class);
            userList = adminService.queryNormalUserByIds(userIds).getData();
        }
        if (userList.size() == 0){
            IExamineManagerUserService examineManagerUserService = ApplicationContextHolder.getBean(IExamineManagerUserService.class);
            userList = examineManagerUserService.queryExamineUser(examineId);
        }
        return userList;
    }


    /**
     * 获取最终的审批流程
     * @param examineFlowVOList
     * @param dataMap
     * @param examineFlowVoS
     * @return void
     **/
    private void getAllConformExamineFlow(List<ExamineFlowVO> examineFlowVOList,Map<String, Object> dataMap,List<ExamineFlowVO> examineFlowVoS){
        for (ExamineFlowVO examineFlowVO : examineFlowVOList) {
            List<ExamineFlowConditionVO> conditionList = examineFlowVO.getConditionList();
            if (CollUtil.isNotEmpty(conditionList)) {
                for (ExamineFlowConditionVO examineFlowConditionVO : conditionList) {
                    List<ExamineFlowConditionDataVO> conditionDataList = examineFlowConditionVO.getConditionDataList();
                    if (CollUtil.isNotEmpty(conditionDataList)) {
                        List<ExamineConditionData> conditions = new ArrayList<>();
                        conditionDataList.forEach(examineFlowConditionDataVO -> {
                            ExamineConditionData examineConditionData = new ExamineConditionData();
                            BeanUtil.copyProperties(examineFlowConditionDataVO,examineConditionData);
                            examineConditionData.setValue(JSON.toJSONString(examineFlowConditionDataVO.getValues()));
                            examineConditionData.setBackupValue(JSON.toJSONString(examineFlowConditionDataVO.getBackupValue()));
                            conditions.add(examineConditionData);
                        });
                        Object createUserId = dataMap.get("createUserId");
                        UserInfo userInfo;
                        if (createUserId != null){
                            userInfo = UserCacheUtil.getUserInfo(TypeUtils.castToLong(createUserId));
                        }else {
                            userInfo = UserUtil.getUser();
                        }
                        boolean isPass = examineConditionService.handleExamineConditionData(conditions, dataMap,userInfo);
                        if (isPass){
                            List<ExamineFlowVO> examineDataList = examineFlowConditionVO.getExamineDataList();
                            if (CollUtil.isNotEmpty(examineDataList)) {
                                this.getAllConformExamineFlow(examineDataList,dataMap,examineFlowVoS);
                            }
                            break;
                        }
                    }
                }
            }else {
                examineFlowVoS.add(examineFlowVO);
            }
        }
    }


    /**
     * 获取所有的节点
     * @param examine
     * @param ownerUserId
     * @return java.util.List<com.kakarote.examine.entity.VO.ExamineFlowVO>
     **/
    private List<ExamineFlowVO> getExamineFlowVOList(Examine examine,Long ownerUserId){
        if (examine == null) {
            return new ArrayList<>();
        }
        String batchId = examine.getBatchId();
        List<ExamineFlow> examineFlowList = examineFlowService.lambdaQuery()
                .eq(ExamineFlow::getBatchId, batchId)
                .list();
        Map<Integer, List<ExamineFlow>> listMap = examineFlowList.stream().collect(Collectors.groupingBy(ExamineFlow::getConditionId));
        //获取条件ID为0的数据，即最上级的审批流
        List<ExamineFlow> examineFlows = listMap.remove(0);
        if (CollUtil.isEmpty(examineFlows)){
            return new ArrayList<>();
        }
        //排序，升序
        examineFlows.sort(((f1, f2) -> f1.getSort() > f2.getSort() ? 1 : -1));
        Map<String, Object> map = new HashMap<>(10, 1.0f);
        map.put("flow", listMap);
        //缓存所有的审批下条件，优化递归查询速度
        for (ExamineTypeEnum examineTypeEnum : ExamineTypeEnum.values()) {
            if (examineTypeEnum.equals(ExamineTypeEnum.MANAGER)) {
                continue;
            }
            ExamineTypeService examineTypeService = ApplicationContextHolder.getBean(examineTypeEnum.getServerName());
            examineTypeService.queryFlowListByBatchId(map, batchId);
        }

        List<ExamineFlowVO> examineFlowVOList = new ArrayList<>();
        List<UserInfo> data = adminService.queryUserInfoList().getData();
        for (ExamineFlow examineFlow : examineFlows) {
            ExamineTypeService examineTypeService = ApplicationContextHolder.getBean(ExamineTypeEnum.valueOf(examineFlow.getExamineType()).getServerName());
            ExamineFlowVO flowInfo = examineTypeService.createFlowInfo(examineFlow, map, data,ownerUserId);
            examineFlowVOList.add(flowInfo);
        }

        return examineFlowVOList;
    }


    @Override
    public BasePage<com.kakarote.core.feign.oa.entity.ExamineVO> queryOaExamineList(ExaminePageBO examinePageBo) {
        UserInfo user = UserUtil.getUser();
        examinePageBo.setLabel(0);
        BasePage<ExamineRecord> basePage = examineRecordLogMapper.selectRecordLogListByUser(examinePageBo.parse(),examinePageBo, user.getUserId(), user.getRoles());
        BasePage<com.kakarote.core.feign.oa.entity.ExamineVO> page = new BasePage<>(basePage.getCurrent(), basePage.getSize(), basePage.getTotal(), basePage.isSearchCount());
        List<com.kakarote.core.feign.oa.entity.ExamineVO> examineVoList = new ArrayList<>();
        for (ExamineRecord examineRecord : basePage.getList()) {
            Integer typeId = examineRecord.getTypeId();
            if(typeId == null){
                continue;
            }
            com.kakarote.core.feign.oa.entity.ExamineVO examineVO = oaService.getOaExamineById(typeId).getData();
            examineVO.setCreateTime(examineRecord.getCreateTime());
            examineVoList.add(examineVO);
        }
        page.setList(examineVoList);
        return page;
    }


    @Override
    public BasePage<ExamineRecordInfoVO> queryCrmExamineList(ExaminePageBO examinePageBo) {
        UserInfo user = UserUtil.getUser();
        BasePage<ExamineRecord> basePage = examineRecordLogMapper.selectRecordLogListByUser(examinePageBo.parse(),examinePageBo, user.getUserId(), user.getRoles());
        BasePage<ExamineRecordInfoVO> page = new BasePage<>(basePage.getCurrent(), basePage.getSize(), basePage.getTotal(), basePage.isSearchCount());
        List<ExamineRecordInfoVO> examineRecordInfoVoS = new ArrayList<>();
        for (ExamineRecord examineRecord : basePage.getList()) {
            ExamineRecordInfoVO examineRecordInfoVO = new ExamineRecordInfoVO();
            BeanUtil.copyProperties(examineRecord,examineRecordInfoVO);
            Integer typeId = examineRecord.getTypeId();
            ExamineConditionDataBO examineConditionDataBO = new ExamineConditionDataBO();
            examineConditionDataBO.setLabel(examinePageBo.getLabel());
            examineConditionDataBO.setTypeId(typeId);
            SimpleCrmInfo simpleCrmInfo = crmExamineService.getCrmSimpleInfo(examineConditionDataBO).getData();
            if (simpleCrmInfo == null){
                continue;
            }
            BeanUtil.copyProperties(simpleCrmInfo,examineRecordInfoVO);
            examineRecordInfoVoS.add(examineRecordInfoVO);
        }
        page.setList(examineRecordInfoVoS);
        return page;
    }

    @Override
    public List<Integer> queryOaExamineIdList(Integer status, Integer categoryId) {
        ExaminePageBO examinePageBo = new ExaminePageBO();
        UserInfo user = UserUtil.getUser();
        examinePageBo.setLabel(0);
        if (status != null && status < 0){
            status = null;
        }
        examinePageBo.setStatus(status);
        examinePageBo.setCategoryId(categoryId);
        return examineRecordLogMapper.selectRecordTypeIdListByUser(examinePageBo, user.getUserId(), user.getRoles());
    }

    @Override
    public List<Integer> queryCrmExamineIdList(Integer label, Integer status) {
        ExaminePageBO examinePageBo = new ExaminePageBO();
        UserInfo user = UserUtil.getUser();
        examinePageBo.setLabel(label);
        examinePageBo.setStatus(status);
        return examineRecordLogMapper.selectRecordTypeIdListByUser(examinePageBo, user.getUserId(), user.getRoles());
    }
}
