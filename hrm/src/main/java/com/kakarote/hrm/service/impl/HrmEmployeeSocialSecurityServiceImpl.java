package com.kakarote.hrm.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.hrm.entity.BO.QuerySalaryListBO;
import com.kakarote.hrm.entity.PO.*;
import com.kakarote.hrm.entity.VO.QuerySalaryListVO;
import com.kakarote.hrm.entity.VO.SalaryOptionHeadVO;
import com.kakarote.hrm.entity.VO.SalarySocialSecurityVO;
import com.kakarote.hrm.mapper.HrmEmployeeSocialSecurityMapper;
import com.kakarote.hrm.mapper.HrmSalaryMonthEmpRecordMapper;
import com.kakarote.hrm.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 员工公积金信息 服务实现类
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
@Service
public class HrmEmployeeSocialSecurityServiceImpl extends BaseServiceImpl<HrmEmployeeSocialSecurityMapper, HrmEmployeeSocialSecurityInfo> implements IHrmEmployeeSocialSecurityService {

    @Autowired
    private IHrmEmployeeSalaryCardService salaryCardService;

    @Autowired
    private IHrmInsuranceSchemeService insuranceSchemeService;

    @Autowired
    private HrmSalaryMonthEmpRecordMapper salaryMonthEmpRecordMapper;

    @Autowired
    private IHrmSalaryMonthOptionValueService salaryMonthOptionValueService;

    @Autowired
    private IHrmSalaryMonthRecordService salaryMonthRecordService;

    @Autowired
    private IHrmInsuranceMonthRecordService insuranceMonthRecordService;

    @Autowired
    private IHrmInsuranceMonthEmpRecordService insuranceMonthEmpRecordService;

    @Override
    public SalarySocialSecurityVO salarySocialSecurityInformation(Integer employeeId) {
        HrmEmployeeSalaryCard employeeSalaryCard = salaryCardService.lambdaQuery().eq(HrmEmployeeSalaryCard::getEmployeeId, employeeId).last("limit 1").one();
        HrmEmployeeSocialSecurityInfo securityInfo = lambdaQuery().eq(HrmEmployeeSocialSecurityInfo::getEmployeeId, employeeId).last("limit 1").one();
        if (securityInfo == null){
            securityInfo = new HrmEmployeeSocialSecurityInfo();
            securityInfo.setEmployeeId(employeeId);
            save(securityInfo);
        }
        if (securityInfo.getSocialSecurityStartMonth() == null){
            Optional<HrmInsuranceMonthEmpRecord> insuranceMonthEmpRecordOpt = insuranceMonthEmpRecordService.lambdaQuery()
                    .eq(HrmInsuranceMonthEmpRecord::getEmployeeId,employeeId).orderByAsc(HrmInsuranceMonthEmpRecord::getCreateTime).last("limit 1")
                    .oneOpt();
            if (insuranceMonthEmpRecordOpt.isPresent()){
                HrmInsuranceMonthEmpRecord insuranceMonthEmpRecord = insuranceMonthEmpRecordOpt.get();
                String socialSecurityStartMonth = insuranceMonthEmpRecord.getYear()+"."+insuranceMonthEmpRecord.getMonth();
                securityInfo.setSocialSecurityStartMonth(socialSecurityStartMonth);
                updateById(securityInfo);
            }
        }
        if (securityInfo.getSchemeId() != null){
            HrmInsuranceScheme insuranceScheme = insuranceSchemeService.lambdaQuery().select(HrmInsuranceScheme::getSchemeName).eq(HrmInsuranceScheme::getSchemeId, securityInfo.getSchemeId()).last("limit 1").one();
            if (insuranceScheme != null){
                securityInfo.setSchemeName(insuranceScheme.getSchemeName());
            }
        }
        return new SalarySocialSecurityVO(employeeSalaryCard,securityInfo);
    }

    @Override
    public void addOrUpdateSalaryCard(HrmEmployeeSalaryCard salaryCard) {
        salaryCardService.saveOrUpdate(salaryCard);
    }

    @Override
    public void deleteSalaryCard(Integer salaryCardId) {
        salaryCardService.removeById(salaryCardId);
    }

    @Override
    public void addOrUpdateSocialSecurity(HrmEmployeeSocialSecurityInfo socialSecurityInfo) {
        saveOrUpdate(socialSecurityInfo);
    }

    @Override
    public void deleteSocialSecurity(Integer socialSecurityInfoId) {
        removeById(socialSecurityInfoId);
    }

    @Override
    public BasePage<QuerySalaryListVO> querySalaryList(QuerySalaryListBO querySalaryListBO) {
        BasePage<QuerySalaryListVO> page = salaryMonthEmpRecordMapper.querySalaryRecord(querySalaryListBO.parse(),querySalaryListBO.getEmployeeId());
        page.getList().forEach(record->{
            Integer sEmpRecordId = record.getSEmpRecordId();
            List<HrmSalaryMonthOptionValue> list = salaryMonthOptionValueService.lambdaQuery()
                    .in(HrmSalaryMonthOptionValue::getCode, Lists.newArrayList(210101, 230101, 240101))
                    .eq(HrmSalaryMonthOptionValue::getSEmpRecordId, sEmpRecordId).list();
            for (HrmSalaryMonthOptionValue salaryMonthOptionValue : list) {
                if (salaryMonthOptionValue.getCode().equals(210101)){
                    record.setShouldSalary(salaryMonthOptionValue.getValue());
                }
                if (salaryMonthOptionValue.getCode().equals(230101)){
                    record.setPersonalTax(salaryMonthOptionValue.getValue());
                }
                if (salaryMonthOptionValue.getCode().equals(240101)){
                    record.setRealSalary(salaryMonthOptionValue.getValue());
                }
            }
        });
        return page;
    }

    @Override
    public List<SalaryOptionHeadVO> querySalaryDetail(String sEmpRecordId) {
        HrmSalaryMonthEmpRecord salaryMonthEmpRecord = salaryMonthEmpRecordMapper.selectById(sEmpRecordId);
        HrmSalaryMonthRecord salaryMonthRecord = salaryMonthRecordService.getById(salaryMonthEmpRecord.getSRecordId());
        String optionHead = salaryMonthRecord.getOptionHead();
        List<SalaryOptionHeadVO> salaryOptionHeadVOList = JSON.parseArray(optionHead, SalaryOptionHeadVO.class);
        List<HrmSalaryMonthOptionValue> list = salaryMonthOptionValueService.lambdaQuery().eq(HrmSalaryMonthOptionValue::getSEmpRecordId, sEmpRecordId).list();
        HashMap<Integer, String> codeMap = new HashMap<>();
        list.forEach(value->{
            codeMap.put(value.getCode(),value.getValue());
        });
        salaryOptionHeadVOList.forEach(option->{
            if (option.getCode().equals(1)){
                option.setValue(salaryMonthEmpRecord.getNeedWorkDay().toString());
            }else if (option.getCode().equals(2)){
                option.setValue(salaryMonthEmpRecord.getActualWorkDay().toString());
            }else {
                option.setValue(codeMap.get(option.getCode()));
            }
        });
        return salaryOptionHeadVOList;
    }
}
