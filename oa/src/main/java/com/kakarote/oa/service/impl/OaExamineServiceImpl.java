package com.kakarote.oa.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import com.kakarote.core.common.FieldEnum;
import com.kakarote.core.common.Result;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.entity.AdminMessageBO;
import com.kakarote.core.feign.admin.entity.AdminMessageEnum;
import com.kakarote.core.feign.admin.entity.SimpleUser;
import com.kakarote.core.feign.admin.service.AdminFileService;
import com.kakarote.core.feign.admin.service.AdminMessageService;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.feign.crm.entity.SimpleCrmEntity;
import com.kakarote.core.feign.crm.service.CrmService;
import com.kakarote.core.feign.examine.entity.ExamineConditionDataBO;
import com.kakarote.core.feign.examine.entity.ExamineInfoVo;
import com.kakarote.core.feign.examine.entity.ExamineRecordReturnVO;
import com.kakarote.core.feign.examine.entity.ExamineRecordSaveBO;
import com.kakarote.core.feign.examine.service.ExamineService;
import com.kakarote.core.field.FieldService;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.servlet.upload.FileEntity;
import com.kakarote.core.utils.TagUtil;
import com.kakarote.core.utils.TransferUtil;
import com.kakarote.core.utils.UserCacheUtil;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.oa.common.FieldUtil;
import com.kakarote.oa.common.OaCodeEnum;
import com.kakarote.oa.entity.BO.AuditExamineBO;
import com.kakarote.oa.entity.BO.ExamineExportBO;
import com.kakarote.oa.entity.BO.ExaminePageBO;
import com.kakarote.oa.entity.BO.GetExamineFieldBO;
import com.kakarote.oa.entity.PO.*;
import com.kakarote.oa.entity.VO.ExamineVO;
import com.kakarote.oa.mapper.OaExamineLogMapper;
import com.kakarote.oa.mapper.OaExamineMapper;
import com.kakarote.oa.service.*;
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
 * @author wyq
 * @since 2020-05-15
 */
@Service
public class OaExamineServiceImpl extends BaseServiceImpl<OaExamineMapper, OaExamine> implements IOaExamineService {

    @Autowired
    private OaExamineMapper examineMapper;

    @Autowired
    private IOaExamineRelationService examineRelationService;

    @Autowired
    private CrmService crmService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private AdminFileService adminFileService;

    @Autowired
    private IOaExamineTravelService examineTravelService;

    @Autowired
    private OaExamineLogMapper examineLogMapper;

    @Autowired
    private IOaExamineCategoryService examineCategoryService;

    @Autowired
    private IOaExamineFieldService examineFieldService;

    @Autowired
    private IOaExamineDataService examineDataService;

    @Autowired
    private IOaExamineStepService examineStepService;

    @Autowired
    private IOaExamineRecordService examineRecordService;

    @Autowired
    private IOaExamineLogService examineLogService;

    @Autowired
    private IOaExamineService oaExamineService;

    @Autowired
    private ExamineService examineService;

    @Autowired
    private FieldService fieldService;

    @Override
    public BasePage<ExamineVO> myInitiate(ExaminePageBO examinePageBO) {
        if(examinePageBO.getCategoryId() != null){
            ExamineInfoVo infoVo = examineService.queryExamineById(examinePageBO.getCategoryId().longValue()).getData();
            examinePageBO.setCategoryId(infoVo != null ? infoVo.getExamineInitId().intValue() : null);
        }
//        BasePage<ExamineVO> page = examineMapper.myInitiate(examinePageBO.parse().setOptimizeCountSql(false), examinePageBO, UserUtil.getUserId(), UserUtil.isAdmin(), null);
        BasePage<ExamineVO> page = examineMapper.myInitiateOaExamine(examinePageBO.parse().setOptimizeCountSql(false), examinePageBO, UserUtil.getUserId(), UserUtil.isAdmin());
        transfer(page.getList());
        return page;
    }

    @Override
    public BasePage<ExamineVO> myOaExamine(ExaminePageBO examinePageBO) {
        BasePage<ExamineVO> page = examineMapper.myOaExamine(examinePageBO.parse(), examinePageBO, UserUtil.getUserId(), UserUtil.isAdmin());
        transfer(page.getList());
        return page;
    }

    @Override
    public List<ExamineVO> transfer(List<ExamineVO> recordList) {
        Long userId = UserUtil.getUserId();
        recordList.forEach(record -> {
            setRelation(record);
            Result<SimpleUser> listResult = adminService.queryUserById(record.getCreateUserId());
            record.setCreateUser(listResult.getData());
            String batchId = record.getBatchId();
            ExamineRecordReturnVO recordReturnVO;
            if (record.getExamineRecordId() != null) {
                recordReturnVO = examineService.queryExamineRecordInfo(record.getExamineRecordId()).getData();
            }else {
                recordReturnVO = new ExamineRecordReturnVO();
                recordReturnVO.setExamineStatus(1);
                recordReturnVO.setExamineUserIds(new ArrayList<>());
            }

            Integer examineStatus = recordReturnVO.getExamineStatus();
            if (examineStatus == null){
                examineStatus = -1;
            }
            record.setExamineStatus(examineStatus);
            List<Long> userIds = recordReturnVO.getExamineUserIds();
            String examineName = "";
            if (CollUtil.isNotEmpty(userIds) && examineStatus != 4) {
                Result<List<SimpleUser>> userList = adminService.queryUserByIds(userIds);
                examineName = userList.getData().stream().map(SimpleUser::getRealname).collect(Collectors.joining(","));
            }
            if (examineStatus == 4){
                examineName = record.getCreateUser().getRealname();
            }

            record.setExamineName(examineName);
            ExamineInfoVo examineInfoVo = examineService.queryExamineById(Long.valueOf(record.getCategoryId())).getData();
            String title = "";
            if(examineInfoVo != null){
                title = examineInfoVo.getExamineName();
                record.setExamineIcon(examineInfoVo.getExamineIcon());
                record.setType(examineInfoVo.getOaType());
            }
            record.setCategoryTitle(title);
            Result<List<FileEntity>> fileList = adminFileService.queryFileList(batchId);
            Map<String, List<FileEntity>> collect = fileList.getData().stream().collect(Collectors.groupingBy(FileEntity::getFileType));
            if (collect.containsKey("img")) {
                record.setImg(collect.get("img"));
            } else {
                record.setImg(new ArrayList<>());
            }
            if (collect.containsKey("file")) {
                record.setFile(collect.get("file"));
            } else {
                record.setFile(new ArrayList<>());
            }
            setCountRecord(record);
            List<Integer> roles = UserUtil.getUser().getRoles();
            Map<String, Integer> permission = new HashMap<>();
            Long createUserId = record.getCreateUserId();
            //超级管理员、创建人可以更新 2审核拒绝 4 已撤回 的审批
            boolean isUpdate = (userId.equals(UserUtil.getSuperUser()) || createUserId.equals(userId) || roles.contains(UserUtil.getSuperRole())) && (examineStatus == 4 || examineStatus == 2);
            if (isUpdate) {
                permission.put("isUpdate", 1);
            } else {
                permission.put("isUpdate", 0);
            }
            //超级管理员可以删除1审核通过 2审核拒绝 4已撤回 的审批，创建人可以删除4 已撤回 的审批
            boolean isSuperDelete = (userId.equals(UserUtil.getSuperUser()) || roles.contains(UserUtil.getSuperRole())) && (examineStatus == 1 || examineStatus == 2 || examineStatus == 4);
            boolean isCreateDelete = createUserId.equals(userId) && (examineStatus == 4);
            if (isSuperDelete || isCreateDelete) {
                permission.put("isDelete", 1);
            } else {
                permission.put("isDelete", 0);
            }
            //创建人可以撤回 0未审核 4审核中 的审批
            boolean isRecheck = createUserId.equals(userId) && (examineStatus == 0 || examineStatus == 3);
            if (isRecheck) {
                permission.put("isRecheck", 1);
            } else {
                permission.put("isRecheck", 0);
            }
            if (record.getExamineStatus() == 2) {
                //如果审批删除,直接审核0
                permission.put("isCheck", 0);
            } else {
                if (examineStatus == 3 && CollUtil.isNotEmpty(userIds) && userIds.contains(userId)) {
                    permission.put("isCheck", 1);
                } else {
                    permission.put("isCheck", 0);
                }
            }
            record.setPermission(permission);
        });
        return recordList;
    }

    private void setRelation(ExamineVO relationRecord) {
        OaExamineRelation relation = examineRelationService.lambdaQuery().eq(OaExamineRelation::getExamineId, relationRecord.getExamineId()).last("limit 1").one();
        if (relation != null) {
            Set<Integer> customerIds = TagUtil.toSet(relation.getCustomerIds());
            if (CollUtil.isNotEmpty(customerIds)) {
                Result<List<SimpleCrmEntity>> listResult = crmService.queryCustomerInfo(customerIds);
                relationRecord.setCustomerList(listResult.getData());
            }
            Set<Integer> contactsIds = TagUtil.toSet(relation.getContactsIds());
            if (CollUtil.isNotEmpty(contactsIds)) {
                Result<List<SimpleCrmEntity>> listResult = crmService.queryContactsInfo(contactsIds);
                relationRecord.setContactsList(listResult.getData());
            }
            Set<Integer> businessIds = TagUtil.toSet(relation.getBusinessIds());
            if (CollUtil.isNotEmpty(businessIds)) {
                Result<List<SimpleCrmEntity>> listResult = crmService.queryBusinessInfo(businessIds);
                relationRecord.setBusinessList(listResult.getData());
            }
            Set<Integer> contractIds = TagUtil.toSet(relation.getContractIds());
            if (CollUtil.isNotEmpty(contractIds)) {
                Result<List<SimpleCrmEntity>> listResult = crmService.queryContractInfo(contractIds);
                relationRecord.setContractList(listResult.getData());
            }
        }
    }

    private void setCountRecord(ExamineVO record) {
        Integer examineId = record.getExamineId();
        String categoryTitle = record.getCategoryTitle();
        Integer count = examineTravelService.lambdaQuery().eq(OaExamineTravel::getExamineId, examineId).count();
        StringBuilder causeTitle = new StringBuilder();
        if (count > 0) {
            causeTitle.append(count);
            switch (categoryTitle) {
                case "出差审批":
                    causeTitle.append("个行程，共");
                    if (record.getDuration() != null) {
                        causeTitle.append(record.getDuration());
                    } else {
                        causeTitle.append(0);
                    }
                    causeTitle.append("天。");
                    break;
                case "差旅报销":
                    causeTitle.append("个报销事项，共");
                    if (record.getMoney() != null) {
                        causeTitle.append(record.getMoney());
                    } else {
                        causeTitle.append(0);
                    }
                    causeTitle.append("元。");
                    break;
                default:
                    break;
            }
        }
        record.setCauseTitle(causeTitle.toString());
    }

    @Override
    public List<OaExamineField> getField(GetExamineFieldBO getExamineFieldBO) {
        OaExamine oaExamineInfo = getById(getExamineFieldBO.getExamineId());
        Integer categoryId = oaExamineInfo.getCategoryId();
        ExamineInfoVo examineInfoVo = examineService.queryExamineById(Long.valueOf(categoryId)).getData();
        List<OaExamineTravel> examineTravelList = examineTravelService.lambdaQuery().eq(OaExamineTravel::getExamineId, oaExamineInfo.getExamineId()).list();
        examineTravelList.forEach(record -> {
            if (StrUtil.isNotEmpty(record.getBatchId())) {
                Result<List<FileEntity>> fileList = adminFileService.queryFileList(record.getBatchId());
                Map<String, List<FileEntity>> listMap = fileList.getData().stream().collect(Collectors.groupingBy(FileEntity::getFileType));
                if (listMap.containsKey("img")) {
                    record.setImg(listMap.get("img"));
                } else {
                    record.setImg(new ArrayList<>());
                }
                if (listMap.containsKey("file")) {
                    record.setFile(listMap.get("file"));
                } else {
                    record.setFile(new ArrayList<>());
                }
            }
        });
        List<OaExamineField> recordList = new ArrayList<>();
        FieldUtil fieldUtil = new FieldUtil(recordList);
        List<String> arr = new ArrayList<>();
        Integer oaType = Optional.ofNullable(examineInfoVo.getOaType()).orElse(0);
        switch (oaType) {
            case 1:
                fieldUtil.oaFieldAdd("content", "审批内容", "text", arr, 1, 0, oaExamineInfo.getContent(), "", 3, 1,"0,0")
                        .oaFieldAdd("remark", "备注", "textarea", arr, 0, 0, oaExamineInfo.getRemark(), "", 3, 1,"1,0");
                break;
            case 2:
                fieldUtil.oaFieldAdd("type_id", "请假类型", "select", Lists.newArrayList("年假", "事假", "病假", "产假", "调休", "婚假", "丧假", "其他"), 1, 0, oaExamineInfo.getTypeId(), "", 3, 1,"0,0")
                        .oaFieldAdd("content", "请假事由", "text", arr, 1, 0, oaExamineInfo.getContent(), "", 3, 1,"1,0")
                        .oaFieldAdd("start_time", "开始时间", "datetime", arr, 1, 0, oaExamineInfo.getStartTime(), "", 3, 1,"2,0")
                        .oaFieldAdd("end_time", "结束时间", "datetime", arr, 1, 0, oaExamineInfo.getEndTime(), "", 3, 1,"2,1")
                        .oaFieldAdd("duration", "时长(天)", "floatnumber", arr, 1, 0, String.valueOf(oaExamineInfo.getDuration()), "", 3, 1,"3,0")
                        .oaFieldAdd("remark", "备注", "textarea", arr, 0, 0, oaExamineInfo.getRemark(), "", 3, 1,"4,0");
                break;
            case 3:
                fieldUtil.oaFieldAdd("content", "出差事由", "text", arr, 1, 0, oaExamineInfo.getContent(), "", 3, 1,"0,0")
                        .oaFieldAdd("remark", "备注", "textarea", arr, 0, 0, oaExamineInfo.getRemark(), "", 3, 1,"1,0")
                        .oaFieldAdd("cause", "行程明细", "business_cause", arr, 1, 0, examineTravelList, "", 3, 1,"2,0")
                        .oaFieldAdd("duration", "出差总天数", "floatnumber", arr, 1, 0, String.valueOf(oaExamineInfo.getDuration()), "", 3, 1,"3,0");
                break;
            case 4:
                fieldUtil.oaFieldAdd("content", "加班原因", "text", arr, 1, 0, oaExamineInfo.getContent(), "", 3, 1,"0,0")
                        .oaFieldAdd("start_time", "开始时间", "datetime", arr, 1, 0, oaExamineInfo.getStartTime(), "", 3, 1,"1,0")
                        .oaFieldAdd("end_time", "结束时间", "datetime", arr, 1, 0, oaExamineInfo.getEndTime(), "", 3, 1,"1,1")
                        .oaFieldAdd("duration", "加班总天数", "floatnumber", arr, 1, 0, oaExamineInfo.getDuration(), "", 3, 1,"2,0")
                        .oaFieldAdd("remark", "备注", "textarea", arr, 0, 0, oaExamineInfo.getRemark(), "", 3, 1,"3,0");
                break;
            case 5:
                fieldUtil.oaFieldAdd("content", "差旅事由", "text", arr, 1, 0, oaExamineInfo.getContent(), "", 3, 1,"0,0")
                        .oaFieldAdd("cause", "费用明细", "examine_cause", arr, 1, 0, examineTravelList, "", 3, 1,"1,0")
                        .oaFieldAdd("money", "报销总金额", "floatnumber", arr, 1, 0, String.valueOf(oaExamineInfo.getMoney()), "", 3, 1,"2,0")
                        .oaFieldAdd("remark", "备注", "textarea", arr, 0, 0, oaExamineInfo.getRemark(), "", 3, 1,"3,0");
                break;
            case 6:
                fieldUtil.oaFieldAdd("content", "借款事由", "text", arr, 1, 0, oaExamineInfo.getContent(), "", 3, 1,"0,0")
                        .oaFieldAdd("money", "借款金额（元）", "floatnumber", arr, 1, 0, String.valueOf(oaExamineInfo.getMoney()), "", 3, 1,"1,0")
                        .oaFieldAdd("remark", "备注", "textarea", arr, 0, 0, oaExamineInfo.getRemark(), "", 3, 1,"2,0");
                break;
            default:
                List<OaExamineField> examineFields = examineFieldService.queryField(categoryId);
                Map<Integer, String> fieldData = examineFieldService.queryFieldData(oaExamineInfo.getBatchId());
                examineFields.forEach(field -> {
                    if ("content".equals(field.getFieldName())) {
                        field.setValue(oaExamineInfo.getContent());
                    } else if ("remark".equals(field.getFieldName())) {
                        field.setValue(oaExamineInfo.getRemark());
                    } else {
                        field.setValue(Optional.ofNullable(fieldData.get(field.getFieldId())).orElse(""));
                    }
                    field.setFormType(FieldEnum.parse(field.getType()).getFormType());
                });
                examineFieldService.transferFieldList(examineFields, getExamineFieldBO.getIsDetail());
                recordList.addAll(examineFields);
                break;
        }
        return fieldUtil.getRecordList();
    }

    @Override
    public List<List<OaExamineField>> getFormPositionField(GetExamineFieldBO getExamineFieldBO){
        List<OaExamineField> oaExamineFields = this.getField(getExamineFieldBO);
        return fieldService.convertFormPositionFieldList(oaExamineFields,OaExamineField::getXAxis,OaExamineField::getYAxis,OaExamineField::getSorting);
    }



    @Override
    public void setOaExamine(JSONObject jsonObject) {
        UserInfo user = UserUtil.getUser();
        OaExamine oaExamine = jsonObject.getObject("oaExamine", OaExamine.class);
        JSONArray oaExamineTravelList = jsonObject.getJSONArray("oaExamineTravelList");
        //报销总金额
        if(oaExamine.getMoney() != null && oaExamine.getMoney().doubleValue() <= 0){
            throw new CrmException(OaCodeEnum.TOTAL_REIMBURSEMENT_ERROR);
        }
        if (oaExamine.getStartTime() != null && oaExamine.getEndTime() != null) {
            if ((oaExamine.getStartTime().compareTo(oaExamine.getEndTime())) >= 0) {
                throw new CrmException(OaCodeEnum.EXAMINE_END_TIME_IS_EARLIER_THAN_START_TIME);
            }
        }
        if (oaExamineTravelList != null) {
            for (int i = 0; i < oaExamineTravelList.size(); i++) {
                Object json = oaExamineTravelList.get(i);
                OaExamineTravel oaExamineTravel = TypeUtils.castToJavaBean(json, OaExamineTravel.class);
                //费用明细金额
                if (oaExamineTravel.getMoney() != null && oaExamineTravel.getMoney().doubleValue() <= 0) {
                    throw new CrmException(OaCodeEnum.TOTAL_AMOUNT_OF_EXPENSE_DETAILS_ERROR,i);
                }
                if (oaExamineTravel.getStartTime() != null && oaExamineTravel.getEndTime() != null) {
                    if ((oaExamineTravel.getStartTime().compareTo(oaExamineTravel.getEndTime())) >= 0) {
                        throw new CrmException(OaCodeEnum.TRAVEL_END_TIME_IS_EARLIER_THAN_START_TIME);
                    }
                }
            }
        }
        String batchId = StrUtil.isNotEmpty(oaExamine.getBatchId()) ? oaExamine.getBatchId() : IdUtil.simpleUUID();
        saveField(jsonObject.getJSONArray("field"), batchId);
        oaExamine.setBatchId(batchId);

        ExamineRecordSaveBO examineRecordSaveBO = jsonObject.getObject("examineFlowData", ExamineRecordSaveBO.class);
        if (oaExamine.getExamineId() == null) {
            save(oaExamine);
            this.supplementFieldInfo(0, oaExamine.getExamineId(), null, examineRecordSaveBO);
            examineRecordSaveBO.setCategoryId(oaExamine.getCategoryId());
            ExamineRecordReturnVO examineData = examineService.addExamineRecord(examineRecordSaveBO).getData();
            oaExamine.setExamineStatus(examineData.getExamineStatus());
            oaExamine.setExamineRecordId(examineData.getRecordId());
            updateById(oaExamine);
        } else {
            OaExamine oldOaExamine = this.getById(oaExamine.getExamineId());
            this.supplementFieldInfo(0,oaExamine.getExamineId(),oldOaExamine.getExamineRecordId(),examineRecordSaveBO);
            examineRecordSaveBO.setCategoryId(oaExamine.getCategoryId());
            ExamineRecordReturnVO examineData = examineService.addExamineRecord(examineRecordSaveBO).getData();
            oaExamine.setExamineStatus(examineData.getExamineStatus());
            oaExamine.setExamineRecordId(examineData.getRecordId());
            oaExamine.setUpdateTime(new Date());
            updateById(oaExamine);
            examineTravelService.lambdaUpdate().eq(OaExamineTravel::getExamineId, oaExamine.getExamineId()).remove();
            examineRelationService.lambdaUpdate().eq(OaExamineRelation::getExamineId, oaExamine.getExamineId()).remove();
        }

        if (jsonObject.get("oaExamineRelation") != null) {
            OaExamineRelation oaExamineRelation = jsonObject.getObject("oaExamineRelation", OaExamineRelation.class);
            oaExamineRelation.setRId(null);
            oaExamineRelation.setBusinessIds(TagUtil.fromString(oaExamineRelation.getBusinessIds()));
            oaExamineRelation.setContactsIds(TagUtil.fromString(oaExamineRelation.getContactsIds()));
            oaExamineRelation.setContractIds(TagUtil.fromString(oaExamineRelation.getContractIds()));
            oaExamineRelation.setCustomerIds(TagUtil.fromString(oaExamineRelation.getCustomerIds()));
            oaExamineRelation.setExamineId(oaExamine.getExamineId());
            oaExamineRelation.setCreateTime(new Date());
            examineRelationService.save(oaExamineRelation);
            crmService.addActivity(2, 9, oaExamine.getExamineId());
        }
        if (oaExamineTravelList != null) {
            for (Object json : oaExamineTravelList) {
                OaExamineTravel oaExamineTravel = TypeUtils.castToJavaBean(json, OaExamineTravel.class);
                oaExamineTravel.setTravelId(null);
                oaExamineTravel.setExamineId(oaExamine.getExamineId());
                examineTravelService.save(oaExamineTravel);
            }
        }
    }


    /**
     * 补充审批字段信息
     *
     * @param label
     * @param typeId
     * @param recordId
     * @param examineRecordSaveBO
     * @return void
     * @date 2020/12/18 13:44
     **/
    public void supplementFieldInfo(Integer label, Integer typeId, Integer recordId, ExamineRecordSaveBO examineRecordSaveBO) {
        examineRecordSaveBO.setLabel(label);
        examineRecordSaveBO.setTypeId(typeId);
        examineRecordSaveBO.setRecordId(recordId);
        if (examineRecordSaveBO.getDataMap() != null) {
            examineRecordSaveBO.getDataMap().put("createUserId", UserUtil.getUserId());
        } else {
            Map<String, Object> entityMap = new HashMap<>(1);
            entityMap.put("createUserId", UserUtil.getUserId());
            examineRecordSaveBO.setDataMap(entityMap);
        }
    }

    /**
     * @param examineType 1 待审核 2 通过 3 拒绝
     */
    public void addMessage(Integer examineType, Object examineObj, Long ownerUserId) {
        AdminMessageBO adminMessageBO = new AdminMessageBO();
        adminMessageBO.setUserId(ownerUserId);
        if (examineType == 1) {
            if (examineObj instanceof OaExamineLog) {
                OaExamineLog examineLog = (OaExamineLog) examineObj;
                OaExamineRecord examineRecord = examineRecordService.getById(examineLog.getRecordId());
                if (examineRecord == null) {
                    return;
                }
                OaExamine examine = getById(examineRecord.getExamineId());
                if (examine == null) {
                    return;
                }
                OaExamineCategory examineCategory = examineCategoryService.getById(examine.getCategoryId());
                if (examineCategory == null) {
                    return;
                }
                adminMessageBO.setTitle(examineCategory.getTitle());
                adminMessageBO.setTypeId(examine.getExamineId());
                adminMessageBO.setMessageType(AdminMessageEnum.OA_EXAMINE_NOTICE.getType());
                adminMessageBO.setIds(Collections.singletonList(examineLog.getExamineUser()));
            }
        } else if (examineType == 2 || examineType == 3) {
            if (examineObj instanceof OaExamineRecord) {
                OaExamineRecord examineRecord = (OaExamineRecord) examineObj;
                OaExamine examine = getById(examineRecord.getExamineId());
                if (examine == null) {
                    return;
                }
                OaExamineCategory examineCategory = examineCategoryService.getById(examine.getCategoryId());
                if (examineCategory == null) {
                    return;
                }
                adminMessageBO.setMessageType(examineType == 2 ? AdminMessageEnum.OA_EXAMINE_PASS.getType() : AdminMessageEnum.OA_EXAMINE_REJECT.getType());
                adminMessageBO.setContent(examineRecord.getRemarks());
                adminMessageBO.setTitle(examineCategory.getTitle());
                adminMessageBO.setTypeId(examine.getExamineId());
                adminMessageBO.setIds(Collections.singletonList(examine.getCreateUserId()));

            }
        }
        if (adminMessageBO.getIds().size() > 0) {
            AdminMessageService messageService = ApplicationContextHolder.getBean(AdminMessageService.class);
            messageService.sendMessage(adminMessageBO);
        }
    }

    /**
     * 保存自定义字段
     *
     * @param array   参数对象
     * @param batchId 批次ID
     */
    private void saveField(JSONArray array, String batchId) {
        if (array == null || StrUtil.isEmpty(batchId)) {
            return;
        }
        examineDataService.lambdaUpdate().eq(OaExamineData::getBatchId, batchId).remove();
        List<OaExamineField> oaExamineFields = JSONArray.parseArray(array.toJSONString(), OaExamineField.class);
        List<OaExamineData> Fieldvs = new ArrayList<>();
        oaExamineFields.forEach(oaExamineField -> {
            OaExamineData fieldv = BeanUtil.copyProperties(oaExamineField, OaExamineData.class);
            fieldv.setValue(fieldService.convertObjectValueToString(oaExamineField.getType(),oaExamineField.getValue(),fieldv.getValue()));
            fieldv.setId(null);
            fieldv.setCreateTime(DateUtil.date());
            fieldv.setBatchId(batchId);
            Fieldvs.add(fieldv);
        });
        examineDataService.saveBatch(Fieldvs, 100);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void oaExamine(AuditExamineBO auditExamineBO) {
        OaExamineLog nowadayExamineLog = new OaExamineLog();
        nowadayExamineLog.setRecordId(auditExamineBO.getRecordId());
        nowadayExamineLog.setExamineStatus(auditExamineBO.getStatus());
        nowadayExamineLog.setRemarks(auditExamineBO.getRemarks());
        Long nextUserId = auditExamineBO.getNextUserId();
        Long auditUserId = UserUtil.getUserId();
        String checkUserIds = "";
        if (nextUserId != null) {
            checkUserIds = nextUserId + "";
        }
        Integer recordId = nowadayExamineLog.getRecordId();
        Integer status = nowadayExamineLog.getExamineStatus();
        //根据审核记录id查询审核记录
        OaExamineRecord examineRecord = examineRecordService.getById(recordId);
        ;
        if (status == 4) {
            if (!examineRecord.getCreateUser().equals(auditUserId) && !auditUserId.equals(UserUtil.getSuperUser())) {
                throw new CrmException(OaCodeEnum.CURRENT_USER_DOES_NOT_HAVE_APPROVAL_AUTHORITY);
            }
        } else {
            OaExamineLog oaExamineLog = examineLogService.lambdaQuery().eq(OaExamineLog::getRecordId, recordId).eq(OaExamineLog::getExamineUser, auditUserId)
                    .eq(examineRecord.getExamineStepId() != null, OaExamineLog::getExamineStepId, examineRecord.getExamineStepId()).eq(OaExamineLog::getExamineStatus, 0)
                    .ne(OaExamineLog::getIsRecheck, 1).orderByDesc(OaExamineLog::getCreateTime).last("limit 1").one();
            //【判断当前审批人是否有审批权限
            if (oaExamineLog == null) {
                throw new CrmException(OaCodeEnum.CURRENT_USER_DOES_NOT_HAVE_APPROVAL_AUTHORITY);
            }
        }
        examineRecord.setExamineStatus(status);
        Integer examineId = examineRecord.getExamineId();
        //查询审批流程
        OaExamine examine = getById(examineId);
        //查询审批类型
        OaExamineCategory examineCategory = examineCategoryService.getById(examine.getCategoryId());
        //查询当前审批步骤
        OaExamineStep examineStep = examineStepService.getById(examineRecord.getExamineStepId());
        ;
        //查询当前审核日志
        Long createUserId = examine.getCreateUserId();
        OaExamineLog log;
        if (examineCategory.getExamineType() == 1) {
            log = examineLogService.lambdaQuery().eq(OaExamineLog::getRecordId, examineRecord.getRecordId())
                    .eq(OaExamineLog::getExamineStepId, examineRecord.getExamineStepId())
                    .eq(OaExamineLog::getExamineUser, auditUserId).eq(OaExamineLog::getIsRecheck, 0).last("limit 1").one();
        } else {
            log = examineLogService.lambdaQuery().eq(OaExamineLog::getRecordId, examineRecord.getRecordId())
                    .eq(OaExamineLog::getExamineUser, auditUserId)
                    .eq(OaExamineLog::getExamineStatus, 0)
                    .eq(OaExamineLog::getIsRecheck, 0)
                    .last("limit 1").one();
        }
        nowadayExamineLog.setExamineUser(auditUserId);
        if (log != null) {
            nowadayExamineLog.setLogId(log.getLogId());
            nowadayExamineLog.setOrderId(log.getOrderId());
        }

        //审核日志 添加审核人
        nowadayExamineLog.setExamineTime(DateUtil.date());
        if (status != 4) {
            examineLogService.updateById(nowadayExamineLog);
        }
        if (status == 2) {
            //判断审核拒绝
            if (examineStep != null && examineStep.getStepType() == 2) {
                Integer toCount = examineLogMapper.queryCountByStepId(recordId, examineStep.getStepId());
                if (toCount == 0) {
                    examineRecord.setExamineStatus(status);
                }
            }
        } else if (status == 4) {
            examineRecord.setExamineStatus(4);
            //先查询该审批流程的审批步骤的第一步
            OaExamineStep oneExamineStep = examineStepService.lambdaQuery().eq(OaExamineStep::getCategoryId, examine.getCategoryId()).orderByAsc(OaExamineStep::getStepNum).last("limit 1").one();
            //判断审核撤回
            OaExamineLog examineLog = new OaExamineLog();
            examineLog.setExamineUser(auditUserId);
            examineLog.setCreateTime(DateUtil.date());
            examineLog.setCreateUser(auditUserId);
            examineLog.setExamineStatus(status);
            examineLog.setIsRecheck(1);
            examineLog.setExamineTime(new Date());
            if (examineCategory.getExamineType() == 1) {
                examineRecord.setExamineStepId(oneExamineStep.getStepId());
                examineLog.setExamineStepId(examineStep.getStepId());
                examineLog.setOrderId(examineStep.getStepNum());
            } else {
                Integer orderId;
                Optional<OaExamineLog> oaExamineLogOpt = examineLogService.lambdaQuery().eq(OaExamineLog::getRecordId, recordId).eq(OaExamineLog::getIsRecheck, 0).ne(OaExamineLog::getExamineStatus, 0)
                        .orderByDesc(OaExamineLog::getOrderId).last("limit 1").oneOpt();
                if (oaExamineLogOpt.isPresent()) {
                    orderId = oaExamineLogOpt.get().getOrderId();
                } else {
                    orderId = 1;
                }
                examineLog.setOrderId(orderId);
            }
            examineLog.setRecordId(examineRecord.getRecordId());
            examineLog.setRemarks(nowadayExamineLog.getRemarks());
            examineLogService.save(examineLog);
            //更新审核日志状态
            examineLogService.lambdaUpdate().set(OaExamineLog::getIsRecheck, 1).eq(OaExamineLog::getRecordId, recordId).update();
        } else {
            //审核通过
            //判断该审批流程类型
            OaExamineStep nextExamineStep = null;
            boolean flag = true;
            if (examineCategory.getExamineType() == 1) {
                //固定审批
                //查询下一个审批步骤
                nextExamineStep = examineStepService.queryExamineStepByNextExamineIdOrderByStepId(examineCategory.getCategoryId(), examineRecord.getExamineStepId());
                //判断是否是并签
                if (examineStep.getStepType() == 3) {
                    //查询当前并签是否都完成
                    //当前并签人员
                    for (Long userId : TagUtil.toLongSet(examineStep.getCheckUserId())) {
                        OaExamineLog examineLog = examineLogService.lambdaQuery().eq(OaExamineLog::getRecordId, examineRecord.getRecordId())
                                .eq(OaExamineLog::getExamineStepId, examineRecord.getExamineStepId()).eq(OaExamineLog::getExamineUser, userId)
                                .ne(OaExamineLog::getIsRecheck, 1).last("limit 1").one();
                        if (examineLog.getExamineStatus() == 0) {
                            //并签未走完
                            flag = false;
                            break;
                        }
                    }
                    //并签未完成
                    if (!flag) {
                        examineRecord.setExamineStatus(3);
                    }
                }
                if (flag) {
                    //判断是否有下一步流程
                    if (nextExamineStep != null) {
                        //有下一步流程
                        examineRecord.setExamineStatus(3);
                        examineRecord.setExamineStepId(nextExamineStep.getStepId());
                        Integer stepType = nextExamineStep.getStepType();
                        //生成审批日志
                        if (stepType == 1) {
                            checkUserIds = adminService.getUserInfo(createUserId).getData().getParentId() + "";
                            ;
                        } else if (stepType == 4) {
                            UserInfo adminUser = adminService.getUserInfo(nowadayExamineLog.getExamineUser()).getData();
                            if (adminUser != null && adminUser.getParentId() != null) {
                                checkUserIds = adminUser.getParentId() + "";
                            } else {
                                checkUserIds = UserUtil.getSuperUser() + "";
                            }
                        } else {
                            checkUserIds = nextExamineStep.getCheckUserId();
                        }
                    }
                    if ("0".equals(checkUserIds)) {
                        checkUserIds = UserUtil.getSuperUser() + "";
                    }
                }
            }
            if (((examineCategory.getExamineType() == 2) || (examineCategory.getExamineType() == 1 && flag)) && StrUtil.isEmpty(checkUserIds)) {
                //没有上级，审核通过
                examineRecord.setExamineStatus(1);
            } else {
                examineRecord.setExamineStatus(3);
                //添加审核日志
                for (Long userId : TagUtil.toLongSet(checkUserIds)) {
                    OaExamineLog oaExamineLog = new OaExamineLog();
                    oaExamineLog.setRecordId(examineRecord.getRecordId());
                    oaExamineLog.setOrderId(nowadayExamineLog.getOrderId() + 1);
                    if (nextExamineStep != null) {
                        oaExamineLog.setOrderId(nextExamineStep.getStepNum());
                        oaExamineLog.setExamineStepId(nextExamineStep.getStepId());
                    }
                    oaExamineLog.setExamineStatus(0);
                    oaExamineLog.setCreateUser(UserUtil.getUserId());
                    oaExamineLog.setCreateTime(new Date());
                    oaExamineLog.setExamineUser(userId);
                    examineLogService.save(oaExamineLog);
                    addMessage(1, oaExamineLog, examine.getCreateUserId());
                }
            }

        }
        examineRecord.setRemarks(nowadayExamineLog.getRemarks());
        if (examineRecord.getExamineStatus().equals(1)) {
            //Aop.get(ActionRecordUtil.class).addOaExamineActionRecord(CrmEnum.OA_EXAMINE, examineId, BehaviorEnum.PASS_EXAMINE);
            addMessage(2, examineRecord, auditUserId);
        } else if (examineRecord.getExamineStatus().equals(2)) {
            //Aop.get(ActionRecordUtil.class).addOaExamineActionRecord(CrmEnum.OA_EXAMINE, examineId, BehaviorEnum.REJECT_EXAMINE);
            addMessage(3, examineRecord, auditUserId);
        }
        examineRecord.setRemarks(null);
        examineRecordService.updateById(examineRecord);
    }

    @Override
    public ExamineVO queryOaExamineInfo(String id) {
        OaExamine oaExamine = examineMapper.selectById(id);
        if (oaExamine == null) {
            throw new CrmException(OaCodeEnum.EXAMINE_ALREADY_DELETE);
        }
        ExamineVO examineVO = new ExamineVO();
        BeanUtil.copyProperties(oaExamine,examineVO);
        SimpleUser simpleUser = adminService.queryUserByIds(Collections.singletonList(examineVO.getCreateUserId())).getData().get(0);
        examineVO.setCreateUser(simpleUser);
        String batchId = oaExamine.getBatchId();
        setRelation(examineVO);
        ExamineInfoVo examineInfoVo = examineService.queryExamineById(Long.valueOf(oaExamine.getCategoryId())).getData();
        String title = "";
        if(examineInfoVo != null){
            title = examineInfoVo.getExamineName();
            examineVO.setExamineIcon(examineInfoVo.getExamineIcon());
            examineVO.setType(examineInfoVo.getOaType());
        }
        examineVO.setCategoryTitle(title);
        Result<List<FileEntity>> fileEntityResult = adminFileService.queryFileList(batchId);
        Map<String, List<FileEntity>> collect = fileEntityResult.getData().stream().collect(Collectors.groupingBy(FileEntity::getFileType));
        if (collect.containsKey("img")) {
            examineVO.setImg(collect.get("img"));
        } else {
            examineVO.setImg(new ArrayList<>());
        }
        if (collect.containsKey("file")) {
            examineVO.setFile(collect.get("file"));
        } else {
            examineVO.setFile(new ArrayList<>());
        }
        List<OaExamineTravel> examineTravelList = examineTravelService.lambdaQuery().eq(OaExamineTravel::getExamineId, id).list();
        examineTravelList.forEach(travel ->
                {
                    if (StrUtil.isNotEmpty(travel.getBatchId())) {
                        List<FileEntity> fileEntities = adminFileService.queryFileList(travel.getBatchId()).getData();
                        Map<String, List<FileEntity>> listMap = fileEntities.stream().collect(Collectors.groupingBy(FileEntity::getFileType));
                        if (listMap.containsKey("img")) {
                            travel.setImg(listMap.get("img"));
                        } else {
                            travel.setImg(new ArrayList<>());
                        }
                        if (listMap.containsKey("file")) {
                            travel.setFile(listMap.get("file"));
                        } else {
                            travel.setFile(new ArrayList<>());
                        }
                    }
                }
        );
        examineVO.setExamineTravelList(examineTravelList);
        return examineVO;
    }

    @Override
    public JSONObject queryExamineRecordList(String recordId) {
        JSONObject jsonObject = new JSONObject();
        JSONObject examineRecord = examineMapper.queryExamineRecordById(recordId);
        Integer examineStatus = examineRecord.getInteger("examineStatus");
        //如果当前审批已撤回
        if (examineRecord.getInteger("examineStatus") == 4) {
            jsonObject.put("examineType", 1);
            JSONObject user = examineLogMapper.queryUserByRecordId(recordId);
            examineRecord.put("userList", user);
            List<JSONObject> records = new ArrayList<>();
            records.add(examineRecord);
            jsonObject.put("steps", records);
            return jsonObject;
        }
        OaExamine oaExamine = getById(examineRecord.getInteger("examineId"));
        OaExamineCategory oaExamineCategory = examineCategoryService.getById(oaExamine.getCategoryId());
        List<JSONObject> list = new ArrayList<>();
        JSONObject rec = examineLogMapper.queryRecordAndId(recordId);
        Long auditUserId = UserUtil.getUserId();
        //jsonObject.put("isRecheck",0);
        //判断是否有撤回权限

        if ((auditUserId.equals(examineRecord.getLong("createUser")) || auditUserId.equals(UserUtil.getSuperUser())) && (examineStatus == 0 || examineStatus == 3)) {
            jsonObject.put("isRecheck", 1);
        } else {
            jsonObject.put("isRecheck", 0);
        }
        if (oaExamineCategory.getExamineType() == 2) {
            JSONObject log = examineLogMapper.queryRecordByUserIdAndStatus(rec.getLong("createUser"), rec.getDate("examineTime"));
            rec.put("examinUser", log);
            list.add(rec);
            //授权审批
            List<JSONObject> logs = examineLogMapper.queryExamineLogAndUserByRecordId(recordId);
            logs.forEach(r -> {
                JSONObject i = examineLogMapper.queryExamineLogAndUserByLogId(r.getInteger("logId"));
                r.put("examinUser", i);
            });
            list.addAll(logs);
            OaExamineLog oaExamineLog = examineLogMapper.queryExamineLog(Integer.valueOf(recordId), auditUserId, examineRecord.getInteger("examineStepId"));
            if (oaExamineLog != null) {
                jsonObject.put("isCheck", 1);
            } else {
                jsonObject.put("isCheck", 0);
            }
            jsonObject.put("examineType", 2);
            jsonObject.put("steps", list);
        } else {
            jsonObject.put("examineType", 1);
            //固定审批
            List<Map<String, Object>> steps = examineStepService.listMaps(new LambdaQueryWrapper<OaExamineStep>()
                    .eq(OaExamineStep::getCategoryId, oaExamineCategory.getCategoryId()).orderByAsc(OaExamineStep::getStepNum));
            steps.forEach(step -> {
                if (ObjectUtil.equal(step.get("stepType"), 1)) {
                    //负责人主管
                    List<JSONObject> logs = examineLogMapper.queryUserByRecordIdAndStepIdAndStatus(recordId, (Long) step.get("stepId"));
                    //已经创建审核日志
                    if (logs != null && logs.size() > 0) {
                        for (JSONObject record : logs) {
                            step.put("examineStatus", record.getInteger("examineStatus"));
                        }
                        step.put("userList", logs);
                    } else {
                        step.put("examineStatus", 0);
                        //还未创建审核日志
                        //查询负责人主管
                        List<JSONObject> r = examineLogMapper.queryUserByUserId(oaExamine.getCreateUserId());
                        if (r == null || r.size() == 0) {
                            r = examineLogMapper.queryUserByUserId(UserUtil.getSuperUser());
                        }
                        step.put("userList", r);
                    }
                } else if (ObjectUtil.equal(step.get("stepType"), 2) || ObjectUtil.equal(step.get("stepType"), 3)) {
                    //先判断是否已经审核过
                    List<JSONObject> logs = examineLogMapper.queryUserByRecordIdAndStepIdAndStatus(recordId, (long) step.get("stepId"));
                    if (logs != null && logs.size() != 0) {
                        //已经创建审核日志
                        int status = 0;
                        if (ObjectUtil.equal(step.get("stepType"), 2)) {
                            Optional<JSONObject> optional = logs.stream().filter(log -> log.getDate("examineTime") != null).min(Comparator.comparingLong(log -> log.getDate("examineTime").getTime()));
                            if (optional.isPresent()) {
                                status = optional.get().getInteger("examineStatus");
                            }
                        }
                        if (ObjectUtil.equal(step.get("stepType"), 3)) {
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
                        for (Long userId : TagUtil.toLongSet((String) step.get("checkUserId"))) {
                            JSONObject jsonObject1 = examineLogMapper.queryUserByUserIdAndStatus(userId);
                            logs.add(jsonObject1);
                        }
                        step.put("examineStatus", 0);
                        step.put("userList", logs);
                    }
                } else {
                    //主管的主管
                    List<JSONObject> logs = examineLogMapper.queryUserByRecordIdAndStepIdAndStatus(recordId, (long) step.get("stepId"));
                    //已经创建审核日志
                    if (logs != null && logs.size() != 0) {
                        for (JSONObject record : logs) {
                            step.put("examineStatus", record.getInteger("examineStatus"));
                        }
                        step.put("userList", logs);
                    } else {
                        step.put("examine_status", 0);
                        //还未创建审核日志
                        //查询负责人主管的主管

                        OaExamineStep examineStep = examineStepService.getById((int) step.get("stepId"));
                        ;
                        Long userId = queryStep(examineStep, Integer.valueOf(recordId), oaExamine.getCreateUserId());
                        //查询负责人主管的主管
                        JSONObject r = examineLogMapper.queryUserByUserIdAndStatus(userId);
                        step.put("userList", Collections.singletonList(r));
                    }
                }
            });
            OaExamineLog oaExamineLog = examineLogMapper.queryExamineLog(Integer.valueOf(recordId), auditUserId, examineRecord.getInteger("examineStepId"));
            if (oaExamineLog != null && (examineRecord.getInteger("examineStatus") == 3 || examineRecord.getInteger("examineStatus") == 0)) {
                jsonObject.put("isCheck", 1);
            } else {
                jsonObject.put("isCheck", 0);
            }
            JSONObject log = examineLogMapper.queryRecordByUserIdAndStatus(rec.getLong("createUser"), rec.getDate("examineTime"));
            rec.put("userList", Collections.singletonList(log));
            rec.put("stepType", 5);
            list.add(rec);
            List<JSONObject> jsonObjects = TransferUtil.transferList(steps, JSONObject.class);
            list.addAll(jsonObjects);
            jsonObject.put("steps", list);
        }
        return jsonObject;
    }

    /**
     * 递归查询上一审核人主管第一级审批步骤
     */
    private Long queryStep(OaExamineStep step, Integer recordId, Long ownerUserId) {
        Optional<OaExamineLog> oaExamineLogOpt = examineLogService.lambdaQuery().eq(OaExamineLog::getRecordId, recordId).eq(OaExamineLog::getExamineStepId, step.getStepId())
                .and(e -> e.eq(OaExamineLog::getIsRecheck, 0).or().isNull(OaExamineLog::getIsRecheck)).eq(OaExamineLog::getExamineStatus, 1)
                .orderByDesc(OaExamineLog::getLogId).last("limit 1").oneOpt();
        if (!oaExamineLogOpt.isPresent()) {
            //获取上一个审核步骤
//            SELECT * FROM 72crm_oa_examine_step WHERE category_id = ?
//            and step_num =  (SELECT step_num FROM 72crm_oa_examine_step where step_id = ?) - 1
            OaExamineStep lastStep = examineStepService.lambdaQuery().eq(OaExamineStep::getCategoryId, step.getCategoryId())
                    .apply("and step_num =  (SELECT step_num FROM 72crm_oa_examine_step where step_id = {0}) - 1", step.getStepId()).last("limit 1").one();
            if (lastStep == null) {
                UserInfo adminUser = adminService.getUserInfo(ownerUserId).getData();
                if (adminUser.getParentId() == null) {
                    return ownerUserId;
                } else {
                    return adminUser.getParentId();
                }
            }
            Long userId = queryStep(lastStep, recordId, ownerUserId);
            if (userId != null) {
                UserInfo adminUser = adminService.getUserInfo(userId).getData();
                if (adminUser.getParentId() == null) {
                    return userId;
                } else {
                    return adminUser.getParentId();
                }
            } else {
                UserInfo adminUser = adminService.getUserInfo(ownerUserId).getData();
                if (adminUser.getParentId() == null) {
                    return ownerUserId;
                } else {
                    return adminUser.getParentId();
                }
            }
        } else {
            //已经生成审核
            OaExamineLog r = oaExamineLogOpt.get();
            UserInfo adminUser = adminService.getUserInfo(r.getExamineUser()).getData();
            if (adminUser.getParentId() == null) {
                return r.getExamineUser();
            } else {
                return adminUser.getParentId();
            }
        }
    }

    @Override
    public List<JSONObject> queryExamineLogList(Integer recordId) {
        OaExamineRecord examineRecord = examineRecordService.getById(recordId);
        OaExamine examine = getById(examineRecord.getExamineId());
        OaExamineCategory category = examineCategoryService.getById(examine.getCategoryId());
        List<JSONObject> logs = new ArrayList<>();
        logs.add(new JSONObject().fluentPut("examine_status", 6).fluentPut("examine_time", examineRecord.getCreateTime()).fluentPut("orderId", 0).
                fluentPut("realname", UserCacheUtil.getUserName(examineRecord.getCreateUser()))
                .fluentPut("remarks", "").fluentPut("user_id", examineRecord.getCreateUser()));
        if (category.getExamineType() == 1) {
            logs.addAll(examineLogMapper.queryExamineLogByRecordIdByStep(recordId));
        } else {
            logs.addAll(examineLogMapper.queryExamineLogByRecordIdByStep1(recordId));
        }
        return logs;
    }

    @Override
    public void deleteOaExamine(Integer examineId) {
        OaExamine examine = getById(examineId);
        Integer recordId = examine.getExamineRecordId();
        boolean bol = examine.getExamineStatus() != 4 && (!UserUtil.isAdmin());
        if (bol) {
            throw new CrmException(SystemCodeEnum.SYSTEM_NO_AUTH);
        }

        examineDataService.lambdaUpdate().eq(OaExamineData::getBatchId, examine.getBatchId()).remove();
        removeById(examineId);
        examineRelationService.lambdaUpdate().eq(OaExamineRelation::getExamineId, examineId).remove();
        examineTravelService.lambdaUpdate().eq(OaExamineTravel::getExamineId, examineId).remove();
        if (recordId != null) {
            examineService.deleteExamineRecord(recordId);
        }
    }

    @Override
    public OaExamineCategory queryExaminStep(String categoryId) {
        OaExamineCategory category = examineCategoryService.getById(categoryId);
        Integer examineType = category.getExamineType();
        if (examineType == 1) {
            List<OaExamineStep> list = examineStepService.lambdaQuery().eq(OaExamineStep::getCategoryId, categoryId).list();
            list.forEach(step -> {
                List<SimpleUser> userList = adminService.queryUserByIds(TagUtil.toLongSet(step.getCheckUserId())).getData();
                step.setUserList(userList);
            });
            category.setStepList(list);
        }
        return category;
    }

    @Override
    public List<Map<String, Object>> export(ExamineExportBO examineExportBO, ExamineInfoVo examineInfoVo , List<OaExamineField> fieldList) {
        Integer type = examineInfoVo.getOaType();
        Integer queryType = examineExportBO.getQueryType();
        List<JSONObject> examineList = new ArrayList<>();
        Long userId = UserUtil.getUserId();
        //我发出的
        if (queryType == 1) {
            examineList = examineMapper.myInitiateOaExcel(examineExportBO, userId, UserUtil.isAdmin());
            //待我审批的
        } else if (queryType == 2) {
            List<Integer> ids = examineService.queryOaExamineIdList(examineExportBO.getStatus(), examineExportBO.getCategoryId()).getData();
            if (CollUtil.isNotEmpty(ids)){
                List<OaExamine> oaExamineList = oaExamineService.lambdaQuery().select(OaExamine::getExamineId, OaExamine::getBatchId,OaExamine::getStartTime,OaExamine::getEndTime).in(OaExamine::getExamineId, ids).list();
                for (OaExamine oaExamine : oaExamineList) {
                    examineList.add((JSONObject) JSON.toJSON(oaExamine));
                }
            }
        }
        List<Integer> examineIdList = examineList.stream().map(record -> record.getInteger("examineId")).collect(Collectors.toList());
        List<String> batchIdList = examineList.stream().map(record -> record.getString("batchId")).collect(Collectors.toList());
        if (examineIdList.size() == 0) {
            examineIdList.add(0);
        }
        if (batchIdList.size() == 0) {
            batchIdList.add("");
        }
        List<JSONObject> recordList;
        //根据审批类型不同 查询sql不同
        if (type == 5 || type == 3) {
            recordList = examineMapper.queryTravelExamineList(examineIdList);
        } else if (type == 0) {
            List<OaExamineField> fields = fieldList.stream().filter(field -> field.getFieldType() != 1).collect(Collectors.toList());
            recordList = examineMapper.queryCustomExamineList(examineIdList, batchIdList, fields);
        } else {
            recordList = examineMapper.queryExamineList(examineIdList);
        }
        List<Map<String, Object>> list = new ArrayList<>();
        switch (type) {
            case 1:
                recordList.forEach(record -> {
                    record.put("category", "普通审批");
                    putRelateCrmWork(record);
                    list.add(record.getInnerMap());
                });
                break;
            case 2:
                recordList.forEach(record -> {
                    record.put("category", "请假审批-" + record.getString("typeId"));
                    putRelateCrmWork(record);
                    list.add(record.getInnerMap());
                });
                break;
            case 3:
                for (int i = 0; i < recordList.size(); i++) {
                    JSONObject record = recordList.get(i);
                    //同一审批，从第二个起前面重复字段不用再显示一遍
                    if (i > 0 && record.getInteger("examineId").equals(recordList.get(i - 1).getInteger("examineId"))) {
                        record.fluentPut("category", "").fluentPut("create_time", "").fluentPut("create_user_name", "").fluentPut("examine_status", "")
                                .fluentPut("examine_user_name", "").fluentPut("previous_examine_user_name", "").fluentPut("content", "")
                                .fluentPut("remark", "").fluentPut("duration", "").fluentPut("relateCrmWork", "");
                    } else {
                        record.put("category", "出差审批");
                        putRelateCrmWork(record);
                    }
                    list.add(record.getInnerMap());
                }
                break;
            case 4:
                recordList.forEach(record -> {
                    record.put("category", "加班审批");
                    putRelateCrmWork(record);
                    list.add(record.getInnerMap());
                });
                break;
            case 5:
                for (int i = 0; i < recordList.size(); i++) {
                    JSONObject record = recordList.get(i);
                    //同一审批，从第二个起前面重复字段不用再显示一遍
                    if (i > 0 && record.getInteger("examineId").equals(recordList.get(i - 1).getInteger("examineId"))) {
                        record.fluentPut("category", "").fluentPut("createTime", "").fluentPut("createUserName", "").fluentPut("examineStatus", "")
                                .fluentPut("examineUserName", "").fluentPut("previousExamineUserName", "").fluentPut("content", "")
                                .fluentPut("money", "").fluentPut("remark", "").fluentPut("relateCrmWork", "");
                    } else {
                        record.fluentPut("category", "差旅报销");
                        putRelateCrmWork(record);
                    }
                    list.add(record.getInnerMap());
                }
                break;
            case 6:
                recordList.forEach(record -> {
                    record.put("category", "借款申请");
                    putRelateCrmWork(record);
                    list.add(record.getInnerMap());
                });
                break;
            case 0:
                recordList.forEach(record -> {
                    record.put("category", examineInfoVo.getExamineName());
                    putRelateCrmWork(record);
                    list.add(record.getInnerMap());
                });
                break;
            default:
                break;
        }
        list.forEach(this::supplementExamineInfoForExport);
        return list;
    }


    private void supplementExamineInfoForExport(Map<String, Object> map){
        Object examineRecordId = map.get("examineRecordId");
        if (examineRecordId == null){
            map.put("examineUserName", "");
            return;
        }
        Object examineStatus = map.get("examineStatusBack");
        if (examineStatus == null) {
            examineStatus = -1;
        }
        ExamineRecordReturnVO recordReturnVO = examineService.queryExamineRecordInfo((Integer) examineRecordId).getData();
        List<Long> userIds = recordReturnVO.getExamineUserIds();
        String examineName = "";
        if (CollUtil.isNotEmpty(userIds) && (Integer) examineStatus != 4) {
            Result<List<SimpleUser>> userList = adminService.queryUserByIds(userIds);
            examineName = userList.getData().stream().map(SimpleUser::getRealname).collect(Collectors.joining(","));
        }
        if ((Integer)examineStatus == 4){
            Object createUserName = map.get("createUserName");
            examineName = createUserName != null ? createUserName.toString() : "";
        }
        map.put("examineUserName", examineName);
    }

    private void putRelateCrmWork(JSONObject record) {
        String relateCrmWork = "";
        if (StrUtil.isNotEmpty(record.getString("customerIds"))) {
            relateCrmWork = relateCrmWork + "客户 【" + record.getString("customerName") + "】\n";
        }
        if (StrUtil.isNotEmpty(record.getString("contactsIds"))) {
            relateCrmWork = relateCrmWork + "联系人 【" + record.getString("contactsName") + "】\n";
        }
        if (StrUtil.isNotEmpty(record.getString("businessIds"))) {
            relateCrmWork = relateCrmWork + "商机 【" + record.getString("businessName") + "】\n";
        }
        if (StrUtil.isNotEmpty(record.getString("contractIds"))) {
            relateCrmWork = relateCrmWork + "合同 【" + record.getString("contractName") + "】\n";
        }
        record.put("relateCrmWork", relateCrmWork.trim());
    }


    @Override
    public Map<String, Object> getDataMapForNewExamine(ExamineConditionDataBO examineConditionDataBO) {
        Map<String, Object> dataMap = new HashMap<>(8);
        Integer id = examineConditionDataBO.getTypeId();
        Integer categoryId = examineConditionDataBO.getCategoryId();
        List<String> fieldList = examineConditionDataBO.getFieldList();
        OaExamine oaExamine = this.getById(id);
        if (oaExamine == null) {
            throw new CrmException(OaCodeEnum.EXAMINE_ALREADY_DELETE);
        }
        List<OaExamineField> examineFields = examineFieldService.queryField(categoryId);
        Map<Integer, String> fieldData = examineFieldService.queryFieldData(oaExamine.getBatchId());
        examineFields.forEach(field -> {
            if ("content".equals(field.getFieldName())) {
                field.setValue(oaExamine.getContent());
            } else if ("remark".equals(field.getFieldName())) {
                field.setValue(oaExamine.getRemark());
            } else {
                field.setValue(Optional.ofNullable(fieldData.get(field.getFieldId())).orElse(""));
            }
        });
        examineFieldService.transferFieldList(examineFields, 2);
        for (String field : fieldList) {
            for (OaExamineField examineField : examineFields) {
                if (field.equals(examineField.getFieldName())) {
                    dataMap.put(field, examineField.getValue());
                    break;
                }
            }
        }
        dataMap.put("createUserId", oaExamine.getCreateUserId());
        return dataMap;
    }

    @Override
    public Boolean updateCheckStatusByNewExamine(ExamineConditionDataBO examineConditionDataBO) {
        Integer typeId = examineConditionDataBO.getTypeId();
        OaExamine oaExamine = oaExamineService.getById(typeId);
        if(oaExamine != null){
            oaExamine.setExamineStatus(examineConditionDataBO.getCheckStatus());
            return oaExamineService.updateById(oaExamine);
        }
        return false;
    }


    @Override
    public ExamineVO getOaExamineById(Integer oaExamineId) {
        OaExamine oaExamine = examineMapper.selectById(oaExamineId);
        ExamineVO examineVO = new ExamineVO();
        if (oaExamine == null){
            return examineVO;
        }
        BeanUtil.copyProperties(oaExamine,examineVO);
        List<ExamineVO> examineVoList = transfer(ListUtil.toList(examineVO));
        return examineVoList.get(0);
    }
}
