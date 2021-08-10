package com.kakarote.hrm.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.hrm.common.EmployeeHolder;
import com.kakarote.hrm.common.HrmCodeEnum;
import com.kakarote.hrm.constant.*;
import com.kakarote.hrm.entity.BO.DeleteLeaveInformationBO;
import com.kakarote.hrm.entity.BO.UpdateInformationBO;
import com.kakarote.hrm.entity.PO.*;
import com.kakarote.hrm.entity.VO.HrmModelFiledVO;
import com.kakarote.hrm.entity.VO.InformationFieldVO;
import com.kakarote.hrm.entity.VO.PostInformationVO;
import com.kakarote.hrm.mapper.HrmEmployeePostMapper;
import com.kakarote.hrm.service.*;
import com.kakarote.hrm.service.actionrecord.impl.EmployeeActionRecordServiceImpl;
import com.kakarote.hrm.utils.EmployeeUtil;
import com.kakarote.hrm.utils.FieldUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 员工证书 服务实现类
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
@Service
public class HrmEmployeePostServiceImpl extends BaseServiceImpl<HrmEmployeePostMapper, HrmEmployeeCertificate> implements IHrmEmployeePostService {

    @Autowired
    private IHrmEmployeeQuitInfoService quitInfoService;

    @Autowired
    private IHrmEmployeeService employeeService;

    @Autowired
    private IHrmEmployeeDataService employeeDataService;

    @Autowired
    private IHrmEmployeeFieldService employeeFieldService;

    @Resource
    private EmployeeActionRecordServiceImpl employeeActionRecordService;

    @Autowired
    private IHrmEmployeeAbnormalChangeRecordService abnormalChangeRecordService;
    @Autowired
    private IHrmFieldExtendService hrmFieldExtendService;

    @Override
    public PostInformationVO postInformation(Integer employeeId) {
        HrmEmployeeQuitInfo employeeQuitInfo = quitInfoService.lambdaQuery().eq(HrmEmployeeQuitInfo::getEmployeeId,employeeId).last("limit 1").one();
        HrmEmployee employee = employeeService.getById(employeeId);
        if (employee.getCompanyAgeStartTime() != null){
            Date companyAgeStartTime = employee.getCompanyAgeStartTime();
            long nowCompanyAge = DateUtil.betweenDay(companyAgeStartTime, new Date(), true)+1;
            if(employee.getCompanyAgeStartTime().getTime()>System.currentTimeMillis()){
                nowCompanyAge=0;
            }
            if (nowCompanyAge != employee.getCompanyAge()){
                employee.setCompanyAge((int) nowCompanyAge);
                employeeService.updateById(employee);
            }
        }
        List<HrmEmployeeData> fieldValueList = employeeDataService.queryListByEmployeeId(employeeId);
        JSONObject employeeModel = BeanUtil.copyProperties(employee, JSONObject.class);
        List<InformationFieldVO> informationFieldVOList = employeeService.transferInformation(employeeModel, LabelGroupEnum.POST, fieldValueList);
        //计算司龄,修改描述
        String companyAgeDesc = EmployeeUtil.computeCompanyAge(employee.getCompanyAge());
        informationFieldVOList.forEach(fieldValue->{
            if ("company_age".equals(fieldValue.getFieldName())){
                fieldValue.setFieldValueDesc(companyAgeDesc);
            }
        });
        if (employee.getEmploymentForms().equals(EmploymentFormsEnum.NO_OFFICIAL.getValue())){
            informationFieldVOList.removeIf(fieldValue-> "probation".equals(fieldValue.getFieldName()));
        }

        return new PostInformationVO(informationFieldVOList,employeeQuitInfo);
    }

    @Override
    public void updatePostInformation(UpdateInformationBO updateInformationBO) {
        Integer employeeId = updateInformationBO.getEmployeeId();
        HrmEmployee oldHrmEmployee=employeeService.getById(employeeId);
        List<UpdateInformationBO.InformationFieldBO> dataList = updateInformationBO.getDataList();
        Map<FiledIsFixedEnum, List<UpdateInformationBO.InformationFieldBO>> isFixedMap = dataList.stream().collect(Collectors.groupingBy(employeeData -> FiledIsFixedEnum.parse(employeeData.getIsFixed())));
        List<UpdateInformationBO.InformationFieldBO> fixedEmployeeData = isFixedMap.get(FiledIsFixedEnum.FIXED);
        JSONObject jsonObject = new JSONObject();
        fixedEmployeeData.forEach(employeeData-> jsonObject.put(employeeData.getFieldName(), FieldUtil.convertFieldValue(employeeData.getType(),employeeData.getFieldValue(), IsEnum.YES.getValue())));
        HrmEmployee employee = jsonObject.toJavaObject(HrmEmployee.class);
        if (employee.getDeptId() == null){
            employeeService.lambdaUpdate().set(HrmEmployee::getDeptId,null).eq(HrmEmployee::getEmployeeId,employeeId).update();
        }
        if (employee.getParentId() == null){
            employeeService.lambdaUpdate().set(HrmEmployee::getParentId,null).eq(HrmEmployee::getEmployeeId,employeeId).update();
        }
        employee.setEmployeeId(employeeId);
        Integer probation = employee.getProbation();
        if (probation != null){
            if (probation == 0) {
                employee.setStatus(EmployeeStatusEnum.OFFICIAL.getValue());
                employee.setBecomeTime(employee.getEntryTime());
            } else {
                employee.setStatus(EmployeeStatusEnum.TRY_OUT.getValue());
                employee.setBecomeTime(DateUtil.offsetMonth(employee.getEntryTime(), probation));
            }
        }
        List<UpdateInformationBO.InformationFieldBO> informationFieldBOS = isFixedMap.get(FiledIsFixedEnum.NO_FIXED);
        if(informationFieldBOS == null) {
            informationFieldBOS = new ArrayList<>();
        }
        List<HrmEmployeeData> hrmEmployeeData = informationFieldBOS.stream()
                .map(field->{
                    Object value = field.getFieldValue();
                    if (value == null){
                        value = "";
                    }
                    field.setFieldValue(employeeFieldService.convertObjectValueToString(field.getType(),field.getFieldValue(),value.toString()));
                    return BeanUtil.copyProperties(field,HrmEmployeeData.class);
                }).collect(Collectors.toList());
        Dict kv = Dict.create().set("key","employee_id").set("param","label_group").set("labelGroup",LabelGroupEnum.POST.getValue()).set("value", employeeId).set("dataTableName", "wk_hrm_employee_data");
        List<HrmModelFiledVO> oldFieldList = ApplicationContextHolder.getBean(IHrmActionRecordService.class).queryFieldValue(kv);
        employeeFieldService.saveEmployeeField(hrmEmployeeData,LabelGroupEnum.POST,employeeId);
        if(null!=oldHrmEmployee.getEntryTime()&&!oldHrmEmployee.getEntryTime().equals(employee.getEntryTime())){
            if(null==employee.getCompanyAgeStartTime()&&null==oldHrmEmployee.getCompanyAgeStartTime()){
                employee.setCompanyAgeStartTime(employee.getEntryTime());
            }
        }
        employeeService.saveOrUpdate(employee);
        //固定字段操作记录保存
        employeeActionRecordService.employeeFixedFieldRecord(BeanUtil.beanToMap(oldHrmEmployee),BeanUtil.beanToMap(employee),LabelGroupEnum.POST, employeeId);
        //非固定字段操作记录保存
        employeeActionRecordService.employeeNOFixedFieldRecord(informationFieldBOS, oldFieldList,employeeId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addOrUpdateLeaveInformation(HrmEmployeeQuitInfo quitInfo) {
        HrmEmployee employee;
        if (quitInfo.getQuitInfoId() == null){
            Integer count = quitInfoService.lambdaQuery().eq(HrmEmployeeQuitInfo::getEmployeeId, quitInfo.getEmployeeId()).count();
            if (count>0){
                throw new CrmException(HrmCodeEnum.THE_EMPLOYEE_HAS_ALREADY_HANDLED_THE_RESIGNATION);
            }
            employeeActionRecordService.quitRecord(quitInfo);
            employee = employeeService.getById(quitInfo.getEmployeeId());
            quitInfo.setOldStatus(employee.getStatus());
        }else {
            employee = new HrmEmployee();
            employee.setEmployeeId(quitInfo.getEmployeeId());
            HrmEmployeeQuitInfo old = quitInfoService.getById(quitInfo.getQuitInfoId());
            employeeActionRecordService.entityUpdateRecord(LabelGroupEnum.QUIT,BeanUtil.beanToMap(old),BeanUtil.beanToMap(quitInfo),quitInfo.getEmployeeId());
        }
        Date planQuitTime = quitInfo.getPlanQuitTime();
        EmployeeEntryStatus entryStatus;
        if (planQuitTime.getTime() > System.currentTimeMillis()){
            entryStatus = EmployeeEntryStatus.TO_LEAVE;
        }else {
            entryStatus = EmployeeEntryStatus.ALREADY_LEAVE;
            abnormalChangeRecordService.addAbnormalChangeRecord(quitInfo.getEmployeeId(),AbnormalChangeType.RESIGNATION,quitInfo.getPlanQuitTime());
        }
        employee.setEntryStatus(entryStatus.getValue());
        employeeService.updateById(employee);
        quitInfoService.saveOrUpdate(quitInfo);
    }

    @Override
    public void deleteLeaveInformation(DeleteLeaveInformationBO deleteLeaveInformationBO) {
        Integer employeeId = deleteLeaveInformationBO.getEmployeeId();
        HrmEmployeeQuitInfo quitInfo = quitInfoService.lambdaQuery().eq(HrmEmployeeQuitInfo::getEmployeeId,employeeId).last("limit 1").one();
        HrmEmployee employee = new HrmEmployee();
        employee.setEmployeeId(quitInfo.getEmployeeId());
        employee.setEntryStatus(EmployeeEntryStatus.IN.getValue());
        employeeService.updateById(employee);
        quitInfoService.removeById(quitInfo.getQuitInfoId());
        employeeActionRecordService.cancelLeave(deleteLeaveInformationBO);
    }

    @Override
    public PostInformationVO postArchives() {
        Integer employeeId = EmployeeHolder.getEmployeeId();
        HrmEmployeeQuitInfo employeeQuitInfo = quitInfoService.lambdaQuery().eq(HrmEmployeeQuitInfo::getEmployeeId,employeeId).last("limit 1").one();
        HrmEmployee employee = employeeService.getById(employeeId);
        List<HrmEmployeeData> fieldValueList = employeeDataService.queryListByEmployeeId(employeeId);
        JSONObject employeeModel = BeanUtil.copyProperties(employee, JSONObject.class);
        List<HrmEmployeeField> list = employeeFieldService.lambdaQuery().eq(HrmEmployeeField::getIsHidden, 0)
                .eq(HrmEmployeeField::getLabelGroup, LabelGroupEnum.POST.getValue())
                .eq(HrmEmployeeField::getIsEmployeeVisible, 1)
                .orderByAsc(HrmEmployeeField::getSorting).list();
        List<InformationFieldVO> informationFieldVOList = employeeService.transferInformation(employeeModel, list, fieldValueList);
        //计算司龄,修改描述
        String companyAgeDesc = EmployeeUtil.computeCompanyAge(employee.getCompanyAge());
        informationFieldVOList.forEach(fieldValue->{
            if ("company_age".equals(fieldValue.getFieldName())){
                fieldValue.setFieldValueDesc(companyAgeDesc);
            }
        });
        if (employee.getEmploymentForms().equals(EmploymentFormsEnum.NO_OFFICIAL.getValue())){
            informationFieldVOList.removeIf(fieldValue-> "probation".equals(fieldValue.getFieldName()));
        }

        return new PostInformationVO(informationFieldVOList,employeeQuitInfo);
    }
}
