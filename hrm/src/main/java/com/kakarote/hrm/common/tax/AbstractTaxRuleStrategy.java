package com.kakarote.hrm.common.tax;

import com.kakarote.hrm.constant.IsEnum;
import com.kakarote.hrm.entity.DTO.ComputeSalaryDTO;
import com.kakarote.hrm.entity.PO.HrmSalaryMonthEmpRecord;
import com.kakarote.hrm.entity.PO.HrmSalaryMonthOptionValue;
import com.kakarote.hrm.entity.PO.HrmSalaryTaxRule;
import com.kakarote.hrm.service.IHrmSalaryMonthEmpRecordService;
import com.kakarote.hrm.service.IHrmSalaryMonthOptionValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 计税策略
 * @author hmb
 */
@Component
public abstract class AbstractTaxRuleStrategy {
    /**
     * 扣代缴项总金额(个人社保+个人公积金等)
     */
    BigDecimal proxyPaySalary;
    /**
     * 应发工资金额
     */
    BigDecimal shouldPaySalary;
    /**
     * 税后补发-税后补扣
     */
    BigDecimal taxAfterPaySalary;

    /**
     * 个税专项附加扣除累计
     */
    BigDecimal taxSpecialGrandTotal;

    /**
     * 特殊计税项
     */
    BigDecimal specialTaxSalary;

    /**
     * 累计免税收入（工资项为加项，且不参与计税的工资）
     */
//    BigDecimal cumulativeTaxFreeIncome;

    @Autowired
    IHrmSalaryMonthEmpRecordService salaryMonthEmpRecordService;

    @Autowired
    IHrmSalaryMonthOptionValueService salaryMonthOptionValueService;
    /**
     * 核算薪资
     * @return 薪资项
     */
    abstract List<HrmSalaryMonthOptionValue> computeSalary(HrmSalaryMonthEmpRecord salaryMonthEmpRecord, HrmSalaryTaxRule taxRule,
                                                            Map<Integer, String> cumulativeTaxOfLastMonthData);

    AbstractTaxRuleStrategy baseComputeSalary(HrmSalaryMonthEmpRecord salaryMonthEmpRecord){
        //所有工资项
        List<ComputeSalaryDTO> computeSalaryDTOS = salaryMonthOptionValueService.querySalaryOptionValue(salaryMonthEmpRecord.getSEmpRecordId());
        //应发工资金额 code = 210101
        //应发工资金额（基本工资+津补贴+浮动工资+奖金+提成工资+计件工资+计时工资+工龄/司龄工资+职称工资+税前补发 - 税前补扣）- 考勤扣款合计+加班工资
        List<Integer> shouldPayCodeList = Arrays.asList(10, 20, 30, 40, 50, 60, 70, 80, 90, 130, 140, 180, 200);
        shouldPaySalary = new BigDecimal(0);
        //加减税总额 (特殊计税项 - 扣代缴项总金额 )
        //扣代缴项总金额(个人社保+个人公积金等)
        proxyPaySalary = new BigDecimal(0);
        //特殊计税项
        specialTaxSalary = new BigDecimal(0);
        //税后补发-税后补扣   parentCode=150 - parentCode=160
        taxAfterPaySalary = new BigDecimal(0);
        //个税专项附加扣除累计 parentCode=260
        taxSpecialGrandTotal = new BigDecimal(0);
        for (ComputeSalaryDTO computeSalaryDTO : computeSalaryDTOS) {
            //计算代扣代缴项总金额
            if (computeSalaryDTO.getParentCode().equals(100)) {
                proxyPaySalary = proxyPaySalary.add(new BigDecimal(computeSalaryDTO.getValue()));
            }
            if (computeSalaryDTO.getParentCode().equals(170)) {
                specialTaxSalary = specialTaxSalary.add(new BigDecimal(computeSalaryDTO.getValue()));
            }
            if (computeSalaryDTO.getParentCode().equals(150)) {
                taxAfterPaySalary = taxAfterPaySalary.add(new BigDecimal(computeSalaryDTO.getValue()));
            }
            if (computeSalaryDTO.getParentCode().equals(160)) {
                taxAfterPaySalary = taxAfterPaySalary.subtract(new BigDecimal(computeSalaryDTO.getValue()));
            }
            if (computeSalaryDTO.getParentCode().equals(260)) {
                taxSpecialGrandTotal = taxSpecialGrandTotal.add(new BigDecimal(computeSalaryDTO.getValue()));
            }
            //计算应发工资(应发工资=员工工资总额-请假扣款-考勤扣款;代扣代缴不需要计算:parentCode=90;企业社保不需要计算:parentCode=110;企业公积金不需要计算:parentCode=110)
            if (shouldPayCodeList.contains(computeSalaryDTO.getParentCode())) {
                if (computeSalaryDTO.getIsPlus() == IsEnum.YES.getValue()) {
                    shouldPaySalary = shouldPaySalary.add(new BigDecimal(computeSalaryDTO.getValue()));
                } else if (computeSalaryDTO.getIsPlus() == IsEnum.NO.getValue()) {
                    shouldPaySalary = shouldPaySalary.subtract(new BigDecimal(computeSalaryDTO.getValue()));
                }
            }
//            if (computeSalaryDTO.getIsCompute() == 1 && computeSalaryDTO.getIsPlus() == 1 && computeSalaryDTO.getIsTax() == 1){
//                cumulativeTaxFreeIncome = cumulativeTaxFreeIncome.add(new BigDecimal(computeSalaryDTO.getValue()));
//            }
        }
        return this;
    }


}
