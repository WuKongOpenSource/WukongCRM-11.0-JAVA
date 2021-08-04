package com.kakarote.hrm.service.actionrecord.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.common.Const;
import com.kakarote.core.common.FieldEnum;
import com.kakarote.core.common.SubModelType;
import com.kakarote.core.common.log.BehaviorEnum;
import com.kakarote.core.common.log.Content;
import com.kakarote.core.common.log.SysLog;
import com.kakarote.core.common.log.SysLogHandler;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.core.utils.UserCacheUtil;
import com.kakarote.hrm.constant.*;
import com.kakarote.hrm.entity.BO.DeleteLeaveInformationBO;
import com.kakarote.hrm.entity.BO.UpdateInformationBO;
import com.kakarote.hrm.entity.PO.*;
import com.kakarote.hrm.entity.VO.HrmModelFiledVO;
import com.kakarote.hrm.service.*;
import com.kakarote.hrm.service.actionrecord.AbstractHrmActionRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 员工操作记录
 *
 * @author hmb
 */
@Service("employeeActionRecordService")
@SysLog(subModel = SubModelType.HRM_EMPLOYEE)
public class EmployeeActionRecordServiceImpl extends AbstractHrmActionRecordService {


    @Autowired
    private IHrmEmployeeService employeeService;

    @Autowired
    private IHrmDeptService deptService;

    @Autowired
    private IHrmInsuranceSchemeService insuranceSchemeService;

    private static HrmActionTypeEnum actionTypeEnum = HrmActionTypeEnum.EMPLOYEE;

    /**
     * 属性kv
     */
    private static Map<LabelGroupEnum, Dict> propertiesMap = new HashMap<>();

    static {
        propertiesMap.put(LabelGroupEnum.EDUCATIONAL_EXPERIENCE, Dict.create().set("education", "学历").set("graduateSchool", "毕业院校").set("major", "专业").set("admissionTime", "入学时间").set("graduationTime", "毕业时间").set("teachingMethods", "教学方式").set("isFirstDegree", "是否第一学历"));
        propertiesMap.put(LabelGroupEnum.WORK_EXPERIENCE, Dict.create().set("workUnit", "工作单位").set("post", "职务").set("workStartTime", "工作开始时间").set("workEndTime", "工作结束时间").set("leavingReason", "离职原因").set("witness", "证明人").set("witnessPhone", "证明人手机号").set("workRemarks", "工作备注"));
        propertiesMap.put(LabelGroupEnum.CERTIFICATE, Dict.create().set("certificateName", "证书名称").set("certificateLevel", "证书级别").set("certificateNum", "证书编号").set("startTime", "有效起始日期").set("endTime", "有效结束日期").set("issuingAuthority", "发证机构").set("issuingTime", "发证日期").set("remarks", "证书备注"));
        propertiesMap.put(LabelGroupEnum.TRAINING_EXPERIENCE, Dict.create().set("trainingCourse", "培训课程").set("trainingOrganName", "培训机构名称").set("startTime", "培训开始时间").set("endTime", "培训结束时间").set("trainingDuration", "培训时长").set("trainingResults", "培训成绩").set("trainingCertificateName", "培训课程名称").set("remarks", "培训备注"));
        propertiesMap.put(LabelGroupEnum.QUIT, Dict.create().set("planQuitTime", "计划离职日期").set("applyQuitTime", "申请离职日期").set("salarySettlementTime", "薪资结算日期").set("quitType", "离职类型").set("quitReason", "离职原因").set("remarks", "备注"));
        propertiesMap.put(LabelGroupEnum.SALARY_CARD, Dict.create().set("salaryCardNum", "工资卡卡号").set("accountOpeningCity", "开户城市").set("bankName", "银行名称").set("openingBank", "工资卡开户行"));
        propertiesMap.put(LabelGroupEnum.SOCIAL_SECURITY, Dict.create().set("isFirstSocialSecurity", "是否首次缴纳社保").set("isFirstAccumulationFund", "是否首次缴纳公积金").set("socialSecurityNum", "社保号").set("accumulationFundNum", "公积金账号").set("socialSecurityStartMonth", "参保起始月份").set("schemeId", "参保方案"));
        propertiesMap.put(LabelGroupEnum.CONTRACT, Dict.create().set("contractNum", "合同编号").set("contractType", "合同类型").set("startTime", "合同开始日期").set("endTime", "合同结束日期").set("term", "期限").set("status", "合同状态").set("signCompany", "签约公司").set("signTime", "合同签订日期").set("remarks", "备注").set("isExpireRemind", "是否到期提醒"));
        propertiesMap.put(LabelGroupEnum.PERSONAL,Dict.create().set("employeeName","姓名").set("mobile","手机").set("country","国家地区").set("nation","民族").set("idType","证件类型").set("idNumber","证件号码").set("sex","性别").set("dateOfBirth","出生日期").set("birthdayType","生日类型").set("birthday","生日").set("age","年龄").set("nativePlace","籍贯").set("address","户籍所在地").set("highestEducation","最高学历"));
        propertiesMap.put(LabelGroupEnum.COMMUNICATION,Dict.create().set("email","邮箱"));
        propertiesMap.put(LabelGroupEnum.POST,Dict.create().set("jobNumber","工号").set("entryTime","入职时间").set("deptId","部门").set("post","岗位").set("parentId","直属上级").set("postLevel","职级").set("workCity","工作城市").set("workAddress","工作地点").set("workDetailAddress","详细工作地点").set("employmentForms","聘用形式").set("probation","试用期").set("becomeTime","转正日期").set("companyAgeStartTime","司龄开始日期").set("companyAge","司龄").set("channelId","招聘渠道").set("status","员工状态"));
        propertiesMap.put(LabelGroupEnum.CONTACT_PERSON,Dict.create().set("","联系人姓名").set("relation","关系").set("contactsPhone","联系人电话").set("contactsWorkUnit","联系人工作单位").set("contactsPost","联系人职务").set("contactsAddress","联系人地址"));
    }
    private List<String> textList = new ArrayList<>();

    /**
     * 新建/删除操作记录
     */
    @SysLogHandler(isReturn = true)
    public Content addOrDeleteRecord(HrmActionBehaviorEnum behaviorEnum, LabelGroupEnum labelGroupEnum, Integer employeeId) {
        String content =  behaviorEnum.getName() + "了" + labelGroupEnum.getDesc();
        actionRecordService.saveRecord(actionTypeEnum, behaviorEnum, Collections.singletonList(content), employeeId);
        HrmEmployee employee = employeeService.getById(employeeId);
        if (HrmActionBehaviorEnum.ADD.equals(behaviorEnum)){
            return new Content(employee.getEmployeeName(),content, BehaviorEnum.SAVE);
        }else {
            return new Content(employee.getEmployeeName(),content,BehaviorEnum.DELETE);
        }
    }


    /**
     * 员工附件附件
     * @param employeeFile
     */
    @SysLogHandler(isReturn = true)
    public Content addFileRecord(HrmEmployeeFile employeeFile,HrmActionBehaviorEnum behaviorEnum) {
        String content;
        if (behaviorEnum.equals(HrmActionBehaviorEnum.ADD)){
            content =  "上传了" + EmployeeFileType.parseName(employeeFile.getSubType())+"附件";
        }else {
            content = behaviorEnum.getName()+ "了" + EmployeeFileType.parseName(employeeFile.getSubType())+"附件";
        }
        actionRecordService.saveRecord(actionTypeEnum, behaviorEnum, Collections.singletonList(content), employeeFile.getEmployeeId());
        HrmEmployee employee = employeeService.getById(employeeFile.getEmployeeId());
        return new Content(employee.getEmployeeName(),content,BehaviorEnum.SAVE);
    }



    /**
     * 员工转正/调岗/晋升
     */
    @SysLogHandler(isReturn = true)
    public Content changeRecord(HrmEmployeeChangeRecord changeRecord) {
        HrmEmployee employee = employeeService.getById(changeRecord.getEmployeeId());
        StringBuilder content = new StringBuilder();
        HrmActionBehaviorEnum changeTypeEnum = HrmActionBehaviorEnum.parse(changeRecord.getChangeType());
        content.append("为").append(employee.getEmployeeName());
        switch (changeTypeEnum) {
            case CHANGE_POST:
            case PROMOTED:
            case DEGRADE:
            case CHANGE_FULL_TIME_EMPLOYEE:
                content
                        .append("添加了一条人事异动【").append(changeTypeEnum.getName()).append("】,异动原因:")
                        .append(ChangeReasonEnum.parseName(changeRecord.getChangeReason()))
                        .append(",生效日期为:").append(DateUtil.formatDate(changeRecord.getEffectTime()));
                String oldDeptValue = "无";
                String newDeptValue = "无";
                if (changeRecord.getOldDept() != null) {
                    oldDeptValue = deptService.getById(changeRecord.getOldDept()).getName();
                }
                if (changeRecord.getNewDept() != null) {
                    newDeptValue = deptService.getById(changeRecord.getNewDept()).getName();
                }
                if (!oldDeptValue.equals(newDeptValue)) {
                    content.append(",部门由").append(oldDeptValue).append("变更为").append(newDeptValue);
                }
                if (changeRecord.getOldPost() == null){
                    changeRecord.setOldPost("无");
                }
                if (!changeRecord.getOldPost().equals(changeRecord.getNewPostLevel())) {
                    content.append(",岗位由").append(changeRecord.getOldPost()).append("变更为").append(changeRecord.getNewPost());
                }
                if (changeRecord.getOldPostLevel() == null){
                    changeRecord.setOldPostLevel("无");
                }
                if (!changeRecord.getOldPostLevel().equals(changeRecord.getNewPostLevel())) {
                    content.append(",职级由").append(changeRecord.getOldPostLevel()).append("变更为").append(changeRecord.getNewPostLevel());
                }
                if (changeRecord.getOldWorkAddress() == null){
                    changeRecord.setOldWorkAddress("无");
                }
                if (!changeRecord.getOldWorkAddress().equals(changeRecord.getNewWorkAddress())) {
                    content.append(",工作地址由").append(changeRecord.getOldWorkAddress()).append("变更为").append(changeRecord.getNewWorkAddress());
                }
                if (changeTypeEnum.equals(HrmActionBehaviorEnum.CHANGE_FULL_TIME_EMPLOYEE)) {
                    content.append(",试用期:").append(changeRecord.getProbation()).append("个月");
                }
                break;
            case BECOME:
                content
                        .append("办理了转正,转正日期").append(DateUtil.formatDate(changeRecord.getEffectTime()));
                break;
            default:
                break;
        }
        if (StrUtil.isNotEmpty(changeRecord.getRemarks())){
            content.append(",备注:").append(changeRecord.getRemarks());
        }
        actionRecordService.saveRecord(actionTypeEnum, changeTypeEnum, Collections.singletonList(content.toString()), changeRecord.getEmployeeId());
        return new Content(employee.getEmployeeName(),content.toString(),BehaviorEnum.UPDATE);
    }

    /**
     * 离职操作记录
     *
     * @param quitInfo
     */
    @SysLogHandler(isReturn = true)
    public Content quitRecord(HrmEmployeeQuitInfo quitInfo) {
        HrmEmployee employee = employeeService.getById(quitInfo.getEmployeeId());
        StringBuilder content = new StringBuilder();
        content
                .append("为").append(employee.getEmployeeName());
        if (quitInfo.getQuitReason() == null) {
            content.append("办理了退休");
        } else {
            QuitReasonEnum reasonEnum = QuitReasonEnum.parse(quitInfo.getQuitReason());
            content.append("添加了离职申请,于").append(DateUtil.formatDate(quitInfo.getApplyQuitTime()))
                    .append("提出离职，计划离职日期：").append(DateUtil.formatDate(quitInfo.getPlanQuitTime()))
                    .append("原因：").append(reasonEnum.getQuitType().getName()).append("-").append(reasonEnum.getName());
        }
        if (StrUtil.isNotEmpty(quitInfo.getRemarks())){
            content.append(",备注:").append(quitInfo.getRemarks());
        }
        actionRecordService.saveRecord(actionTypeEnum, HrmActionBehaviorEnum.QUIT, Collections.singletonList(content.toString()), quitInfo.getEmployeeId());
        return new Content(employee.getEmployeeName(),content.toString(),BehaviorEnum.UPDATE);
    }


    /**
     * 员工个人信息其他表更新 具体看 labelGroupEnum
     *
     * @param labelGroupEnum
     * @param oldRecord
     * @param newRecord
     * @param employeeId
     */
    @SysLogHandler(isReturn = true)
    public Content entityUpdateRecord(LabelGroupEnum labelGroupEnum, Map<String, Object> oldRecord, Map<String, Object> newRecord, Integer employeeId) {
        Dict properties = propertiesMap.get(labelGroupEnum);
        List<String> contentList = entityCommonUpdateRecord(labelGroupEnum, properties, oldRecord, newRecord);
        actionRecordService.saveRecord(actionTypeEnum, HrmActionBehaviorEnum.UPDATE, contentList, employeeId);
        HrmEmployee employee = employeeService.getById(employeeId);
        return new Content(employee.getEmployeeName(), CollUtil.join(contentList,","),BehaviorEnum.UPDATE);
    }

    /**
     * 修改参保方案
     */
    @SysLogHandler(isReturn = true)
    public Content updateSchemeRecord(HrmInsuranceScheme oldInsuranceScheme, HrmInsuranceScheme newInsuranceScheme, HrmEmployee employee) {
        String oldSchemeName;
        String newSchemeName;
        if (oldInsuranceScheme == null) {
            oldSchemeName = "空";
        } else {
            oldSchemeName = oldInsuranceScheme.getSchemeName();
        }
        if (newInsuranceScheme == null) {
            newSchemeName = "空";
        } else {
            newSchemeName = newInsuranceScheme.getSchemeName();
        }
        String content =
                "为" + employee.getEmployeeName() +
                "修改了参保方案,由【" +
                oldSchemeName + "】修改为【" +
                newSchemeName + "】";
        actionRecordService.saveRecord(actionTypeEnum, HrmActionBehaviorEnum.UPDATE, Collections.singletonList(content), employee.getEmployeeId());
        return new Content(employee.getEmployeeName(), content,BehaviorEnum.UPDATE);
    }


    @Override
    protected String compare(LabelGroupEnum labelGroupEnum, Dict properties, String newFieldKey, String oldValue, String newValue) {
        String content = super.compare(labelGroupEnum, properties, newFieldKey, oldValue, newValue);
        switch (labelGroupEnum) {
            case EDUCATIONAL_EXPERIENCE:
                if ("education".equals(newFieldKey)) {
                    if (!"空".equals(oldValue)){
                        oldValue = EmployeeEducationEnum.parseName(Integer.parseInt(oldValue));
                    }
                    if (!"空".equals(newValue)){
                        newValue = EmployeeEducationEnum.parseName(Integer.parseInt(newValue));
                    }
                    content = "将" + properties.getStr(newFieldKey) + "由" + oldValue + "改为" + newValue;
                } else if ("teachingMethods".equals(newFieldKey)) {
                    if (!"空".equals(oldValue)){
                        oldValue = TeachingMethodsEnum.parseName(Integer.parseInt(oldValue));
                    }
                    if (!"空".equals(newValue)){
                        newValue = TeachingMethodsEnum.parseName(Integer.parseInt(newValue));
                    }
                    content = "将" + properties.getStr(newFieldKey) + "由" + oldValue + "改为" + newValue;
                } else if ("isFirstDegree".equals(newFieldKey)) {
                    if (!"空".equals(oldValue)){
                        oldValue = IsEnum.parseName(Integer.parseInt(oldValue));
                    }
                    if (!"空".equals(newValue)){
                        newValue = IsEnum.parseName(Integer.parseInt(newValue));
                    }
                    content = "将" + properties.getStr(newFieldKey) + "由" + oldValue + "改为" + newValue;
                }
                break;
            case QUIT:
                if ("quitType".equals(newFieldKey)) {
                    if (!"空".equals(oldValue)){
                        oldValue = QuitTypeEnum.parseName(Integer.parseInt(oldValue));
                    }
                    if (!"空".equals(newValue)){
                        newValue = QuitTypeEnum.parseName(Integer.parseInt(newValue));
                    }
                    content = "将" + properties.getStr(newFieldKey) + "由" + oldValue + "改为" + newValue;
                } else if ("quitReason".equals(newFieldKey)) {
                    if (!"空".equals(oldValue)){
                        oldValue = QuitReasonEnum.parseName(Integer.parseInt(oldValue));
                    }
                    if (!"空".equals(newValue)){
                        newValue = QuitReasonEnum.parseName(Integer.parseInt(newValue));
                    }
                    content = "将" + properties.getStr(newFieldKey) + "由" + oldValue + "改为" + newValue;
                }
                break;
            case SOCIAL_SECURITY:
                if ("isFirstSocialSecurity".equals(newFieldKey)) {
                    if (!"空".equals(oldValue)){
                        oldValue = IsEnum.parseName(Integer.parseInt(oldValue));
                    }
                    if (!"空".equals(newValue)){
                        newValue = IsEnum.parseName(Integer.parseInt(newValue));
                    }
                    content = "将" + properties.getStr(newFieldKey) + "由" + oldValue + "改为" + newValue;
                } else if ("isFirstAccumulationFund".equals(newFieldKey)) {
                    if (!"空".equals(oldValue)){
                        oldValue = IsEnum.parseName(Integer.parseInt(oldValue));
                    }
                    if (!"空".equals(newValue)){
                        newValue = IsEnum.parseName(Integer.parseInt(newValue));
                    }
                    content = "将" + properties.getStr(newFieldKey) + "由" + oldValue + "改为" + newValue;
                } else if ("schemeId".equals(newFieldKey)) {
                    HrmInsuranceScheme oldScheme = insuranceSchemeService.getById(oldValue);
                    HrmInsuranceScheme newScheme = insuranceSchemeService.getById(newValue);
                    if (oldScheme != null){
                        oldValue = oldScheme.getSchemeName();
                    }
                    if (newScheme != null){
                        newValue = newScheme.getSchemeName();
                    }
                    content = "将" + properties.getStr(newFieldKey) + "由" + oldValue + "改为" + newValue;
                }
                break;
            case CONTRACT:
                if ("contractType".equals(newFieldKey)) {
                    if (!"空".equals(oldValue)){
                        oldValue = EmployeeContractType.parseName(Integer.parseInt(oldValue));
                    }
                    if (!"空".equals(newValue)){
                        newValue = EmployeeContractType.parseName(Integer.parseInt(newValue));
                    }
                    content = "将" + properties.getStr(newFieldKey) + "由" + oldValue + "改为" + newValue;
                } else if ("status".equals(newFieldKey)) {
                    if (!"空".equals(oldValue)){
                        oldValue = EmployeeContractStatus.parseName(Integer.parseInt(oldValue));
                    }
                    if (!"空".equals(newValue)){
                        newValue = EmployeeContractStatus.parseName(Integer.parseInt(newValue));
                    }
                    content = "将" + properties.getStr(newFieldKey) + "由" + oldValue + "改为" + newValue;
                } else if ("isExpireRemind".equals(newFieldKey)) {
                    if (!"空".equals(oldValue)){
                        oldValue = IsEnum.parseName(Integer.parseInt(oldValue));
                    }
                    if (!"空".equals(newValue)){
                        newValue = IsEnum.parseName(Integer.parseInt(newValue));
                    }
                    content = "将" + properties.getStr(newFieldKey) + "由" + oldValue + "改为" + newValue;
                }
                break;
            default:
                break;
        }
        return content;
    }

    @SysLogHandler(isReturn = true)
    public Content cancelLeave(DeleteLeaveInformationBO deleteLeaveInformationBO){
        StringBuilder content = new StringBuilder();
        content.append("取消了离职");
        if (StrUtil.isNotEmpty(deleteLeaveInformationBO.getRemarks())){
            content.append(",原因:").append(deleteLeaveInformationBO.getRemarks());
        }
        actionRecordService.saveRecord(actionTypeEnum, HrmActionBehaviorEnum.UPDATE, Collections.singletonList(content.toString()), deleteLeaveInformationBO.getEmployeeId());
        HrmEmployee employee = employeeService.getById(deleteLeaveInformationBO.getEmployeeId());
        return new Content(employee.getEmployeeName(), content.toString(),BehaviorEnum.UPDATE);
    }

    /**
     * 保存固定字段记录
     * @param oldObj
     * @param newObj
     * @param labelGroupEnum
     * @param employeeId
     */
    @SysLogHandler(isReturn = true)
    public  Content employeeFixedFieldRecord(Map<String, Object> oldObj, Map<String, Object> newObj, LabelGroupEnum labelGroupEnum, Integer employeeId){
        HrmEmployee employee = employeeService.getById(employeeId);
        try {
            textList.clear();
            searchChange(textList, oldObj, newObj, labelGroupEnum);
            if (textList.size() > 0) {
                actionRecordService.saveRecord(actionTypeEnum, HrmActionBehaviorEnum.UPDATE, textList, employeeId);
                return new Content(employee.getEmployeeName(), CollUtil.join(textList,","),BehaviorEnum.UPDATE);
            }

        } finally {
            textList.clear();
        }
        return new Content(employee.getEmployeeName(),"",BehaviorEnum.UPDATE);
    }

    /**
     * 保存非固定字段记录
     * @param newFieldList
     * @param kv
     */
    @SysLogHandler(isReturn = true)
    public Content  employeeNOFixedFieldRecord(List<UpdateInformationBO.InformationFieldBO> newFieldList, Dict kv, Integer employeeId){
        HrmEmployee employee = employeeService.getById(employeeId);
        textList.clear();
        if (newFieldList == null) {
           return new Content(employee.getEmployeeName(),"",BehaviorEnum.UPDATE);
        }
        List<HrmModelFiledVO> oldFieldList = ApplicationContextHolder.getBean(IHrmActionRecordService.class).queryFieldValue(kv);
        newFieldList.forEach(newField -> {
            for (HrmModelFiledVO oldField : oldFieldList) {
                if (oldField.getFieldId().equals(newField.getFieldId())) {
                    if (ObjectUtil.isEmpty(oldField.getFieldValue()) && ObjectUtil.isEmpty(newField.getFieldValue())) {
                        continue;
                    }
                    if(Objects.equals(FieldEnum.parse(oldField.getType()),FieldEnum.DETAIL_TABLE)){
                        parseDetailTable(oldField.getFieldValue(),newField.getFieldValue(),oldField.getName(),oldField.getType(),textList);
                    }else{
                        String oldFieldValue = (String) parseValue(oldField.getFieldValue(),oldField.getType(),true);
                        String newFieldValue = (String) parseValue(newField.getFieldValue(),newField.getType(),true);
                        if (!oldFieldValue.equals(newFieldValue)) {
                            textList.add("将" + oldField.getName() + " 由" + oldFieldValue + "修改为" + newFieldValue + "。");
                        }
                    }
                }
            }
        });
        if (textList.size() > 0) {
            actionRecordService.saveRecord(actionTypeEnum, HrmActionBehaviorEnum.UPDATE, textList, employeeId);
            return new Content(employee.getEmployeeName(), CollUtil.join(textList,","),BehaviorEnum.UPDATE);
        }
        return new Content(employee.getEmployeeName(),"",BehaviorEnum.UPDATE);
    }
    public static Object parseValue(Object value, Integer type,boolean isNeedStr) {
        if (ObjectUtil.isEmpty(value) && !type.equals(FieldEnum.DETAIL_TABLE.getType())) {
            return isNeedStr ? "空" : "";
        }
        FieldEnum fieldEnum = FieldEnum.parse(type);
        switch (fieldEnum) {
            case BOOLEAN_VALUE: {
                return "1".equals(value) ? "是" : "否";
            }
            case AREA_POSITION: {
                StringBuilder stringBuilder = new StringBuilder();
                if (value instanceof CharSequence) {
                    value = JSON.parseArray(value.toString());
                }
                for (Map<String, Object> map : ((List<Map<String, Object>>) value)) {
                    stringBuilder.append(map.get("name")).append(" ");
                }
                return stringBuilder.toString();
            }
            case CURRENT_POSITION: {
                if (value instanceof CharSequence) {
                    value = JSON.parseObject(value.toString());
                }
                return Optional.ofNullable(((Map<String, Object>) value).get("address")).orElse("").toString();
            }
            case DATE_INTERVAL: {
                if (value instanceof Collection) {
                    value = StrUtil.join(Const.SEPARATOR,value);
                }
                return isNeedStr ? value.toString() : value;
            }
            case SINGLE_USER:
            case USER:
                List<String> ids = StrUtil.splitTrim(value.toString(),Const.SEPARATOR);
                return ids.stream().map(id -> UserCacheUtil.getUserName(Long.valueOf(id))).collect(Collectors.joining(Const.SEPARATOR));
            case STRUCTURE:
                List<String> deptIds = StrUtil.splitTrim(value.toString(),Const.SEPARATOR);
                return deptIds.stream().map(id -> UserCacheUtil.getDeptName(Integer.valueOf(id))).collect(Collectors.joining(Const.SEPARATOR));
            case DETAIL_TABLE: {
                if(value == null) {
                    value = new ArrayList<>();
                }
                if(value instanceof String){
                    if("".equals(value)) {
                        value = new ArrayList<>();
                    } else {
                        value = JSON.parseArray((String) value);
                    }
                }
                List<Map<String,Object>> list = new ArrayList<>();
                JSONArray array = new JSONArray((List<Object>) value);
                for (int i = 0; i < array.size(); i++) {
                    JSONArray objects = array.getJSONArray(i);
                    for (int j = 0; j < objects.size(); j++) {
                        Map <String,Object> map = new HashMap<>();
                        JSONObject data = objects.getJSONObject(j);
                        map.put("name",data.get("name"));
                        map.put("value",parseValue(data.get("value"),data.getInteger("type"),false));
                        list.add(map);
                    }
                }
                return list;
            }
            default:
                return isNeedStr ? value.toString() : value;
        }

    }

    /**
     * 明细表格装换
     * @param oldFieldValue
     * @param newFieldValue
     * @param name
     * @param type
     * @param textList
     */
    public static void parseDetailTable(Object oldFieldValue,Object newFieldValue,String name,Integer type,List<String> textList){
        List<Map<String,Object>> oldFieldValues = (List<Map<String, Object>>) parseValue(oldFieldValue,type,false);
        List<Map<String,Object>> newFieldValues = (List<Map<String, Object>>) parseValue(newFieldValue,type,false);
        int size = oldFieldValues.size() >= newFieldValues.size() ? oldFieldValues.size() : newFieldValues.size();
        for (int i = 0; i < size; i++) {
            Object oldValue = oldFieldValues.size() > i ? oldFieldValues.get(i).get("value") :"";
            Object newValue = newFieldValues.size() > i ? newFieldValues.get(i).get("value") :"";
            if (!Objects.equals(oldValue,newValue)) {
                Map<String,Object> map = oldFieldValues.size() >= newFieldValues.size() ? oldFieldValues.get(i) : newFieldValues.get(i);
                textList.add("将" + name +" "+ map.get("name") + " 由" + oldValue + "修改为" + newValue + "。");
            }
        }
    }

    /**
     * 对比出变更字段
     * @param textList 不同字段
     * @param oldObj
     * @param newObj
     * @param hrmTypes 类型
     */
    private void searchChange(List<String> textList, Map<String, Object> oldObj, Map<String, Object> newObj,  LabelGroupEnum hrmTypes) {
        for (String oldKey : oldObj.keySet()) {
            for (String newKey : newObj.keySet()) {
                if (propertiesMap.get(hrmTypes).containsKey(oldKey)) {
                    Object oldValue = oldObj.get(oldKey);
                    Object newValue = newObj.get(newKey);
                    if (oldValue instanceof Date) {
                        oldValue = DateUtil.formatDateTime((Date) oldValue);
                    }
                    if (newValue instanceof Date) {
                        newValue = DateUtil.formatDateTime((Date) newValue);
                    }
                    if (ObjectUtil.isEmpty(oldValue) || ("address".equals(oldKey))) {
                        oldValue = "空";
                    }
                    if (ObjectUtil.isEmpty(newValue) || ("address".equals(newKey))) {
                        newValue = "空";
                    }
                    if (oldValue instanceof BigDecimal || newValue instanceof BigDecimal) {
                        oldValue = Convert.toBigDecimal(oldValue, new BigDecimal(0)).setScale(2, BigDecimal.ROUND_UP).toString();
                        newValue = Convert.toBigDecimal(newValue, new BigDecimal(0)).setScale(2, BigDecimal.ROUND_UP).toString();
                    }
                    if (newKey.equals(oldKey) && !Objects.equals(oldValue,newValue)) {
                        switch (oldKey) {
                            case "idType":
                                if (!"空".equals(newValue)) {
                                    newValue = IdTypeEnum.parseName(Integer.valueOf(newValue.toString()));
                                }
                                if (!"空".equals(oldValue)) {
                                    oldValue = IdTypeEnum.parseName(Integer.valueOf(oldValue.toString()));
                                }
                                break;
                            case "sex":
                                if (!"空".equals(newValue)) {
                                    newValue = newValue.equals(1) ? "男" : "女";
                                }
                                if (!"空".equals(oldValue)) {
                                    oldValue = oldValue.equals(1) ? "男" : "女";
                                }
                                break;
                            case "probation":
                                if (!"空".equals(newValue)) {
                                    newValue = newValue.equals(0) ? "无试用期" : newValue;
                                }
                                if (!"空".equals(oldValue)) {
                                    oldValue = oldValue.equals(0) ? "无试用期" :oldValue;
                                }
                                break;
                            case "birthdayType":
                                if (!"空".equals(newValue)) {
                                    newValue = newValue.equals(1) ? "阳历" : "农历";
                                }
                                if (!"空".equals(oldValue)) {
                                    oldValue = oldValue.equals(1) ? "阳历" : "农历";
                                }
                                break;
                            case "employmentforms":
                                if (!"空".equals(newValue)) {
                                    newValue = newValue.equals(1) ? "正式" : "非正式";
                                }
                                if (!"空".equals(oldValue)) {
                                    oldValue = oldValue.equals(1) ? "正式" : "非正式";
                                }
                                break;
                            case "parentId":
                                if (!"空".equals(newValue)) {
                                    HrmEmployee newHrmEmployee =ApplicationContextHolder.getBean(IHrmEmployeeService.class).getById(newValue.toString());
                                    if(null!=newHrmEmployee){
                                        newValue =newHrmEmployee.getEmployeeName();
                                    }
                                }
                                if (!"空".equals(oldValue)) {
                                    HrmEmployee oldHrmEmployee =ApplicationContextHolder.getBean(IHrmEmployeeService.class).getById(oldValue.toString());
                                    if(null!=oldHrmEmployee){
                                        oldValue =oldHrmEmployee.getEmployeeName();
                                    }
                                }
                                break;
                            case "channelId":
                                if (!"空".equals(newValue)) {
                                    HrmRecruitChannel newChannel=ApplicationContextHolder.getBean(IHrmRecruitChannelService.class).getById(newValue.toString());
                                    if (newChannel != null){
                                        newValue = newChannel.getValue();
                                    }
                                }
                                if (!"空".equals(oldValue)) {
                                    HrmRecruitChannel oldChannel =ApplicationContextHolder.getBean(IHrmRecruitChannelService.class).getById(oldValue.toString());
                                    if (oldChannel != null){
                                        oldValue = oldChannel.getValue();
                                    }
                                }
                                break;
                            default:
                                break;
                        }
                        if (ObjectUtil.isEmpty(oldValue)) {
                            oldValue = "空";
                        }
                        if (ObjectUtil.isEmpty(newValue)) {
                            newValue = "空";
                        }
                        textList.add("将" + propertiesMap.get(hrmTypes).get(oldKey) + " 由" + oldValue + "修改为" + newValue + "。");
                    }
                }
            }
        }
    }
}
