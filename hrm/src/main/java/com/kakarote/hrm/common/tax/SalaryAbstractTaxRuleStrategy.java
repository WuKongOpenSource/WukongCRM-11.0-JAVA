package com.kakarote.hrm.common.tax;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;
import com.kakarote.hrm.entity.PO.HrmSalaryMonthEmpRecord;
import com.kakarote.hrm.entity.PO.HrmSalaryMonthOptionValue;
import com.kakarote.hrm.entity.PO.HrmSalaryTaxRule;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 工资薪金策略
 *
 * @author hmb
 */
@Component("SALARY_TAX_TYPE")
public class SalaryAbstractTaxRuleStrategy extends AbstractTaxRuleStrategy {

    private static RangeMap<BigDecimal, TaxEntity> taxRateRangeMap = TreeRangeMap.create();

    static {
        taxRateRangeMap.put(Range.lessThan(new BigDecimal(0)), new TaxEntity(0, 0));
        taxRateRangeMap.put(Range.closed(new BigDecimal(0), new BigDecimal(36000)), new TaxEntity(3, 0));
        taxRateRangeMap.put(Range.openClosed(new BigDecimal(36000), new BigDecimal(144000)), new TaxEntity(10, 2520));
        taxRateRangeMap.put(Range.openClosed(new BigDecimal(144000), new BigDecimal(300000)), new TaxEntity(20, 16920));
        taxRateRangeMap.put(Range.openClosed(new BigDecimal(300000), new BigDecimal(420000)), new TaxEntity(25, 31920));
        taxRateRangeMap.put(Range.openClosed(new BigDecimal(420000), new BigDecimal(660000)), new TaxEntity(30, 52920));
        taxRateRangeMap.put(Range.openClosed(new BigDecimal(660000), new BigDecimal(960000)), new TaxEntity(35, 85920));
        taxRateRangeMap.put(Range.atLeast(new BigDecimal(960000)), new TaxEntity(45, 181920));
    }

    @Override
    public List<HrmSalaryMonthOptionValue> computeSalary(HrmSalaryMonthEmpRecord salaryMonthEmpRecord, HrmSalaryTaxRule taxRule,
                                                          Map<Integer, String> cumulativeTaxOfLastMonthData) {
        List<HrmSalaryMonthOptionValue> salaryMonthOptionValueList = new ArrayList<>();
        BigDecimal shouldTaxSalary = new BigDecimal(0);
        //基础应税工资-不包含每月减除费用(应发工资 + 特殊计税项 - 扣代缴项总金额 ) code = 210101
        BigDecimal baseShouldTaxSalary = shouldPaySalary.add(specialTaxSalary).subtract(proxyPaySalary);
        if (baseShouldTaxSalary.compareTo(new BigDecimal(taxRule.getMarkingPoint())) > 0) {
            shouldTaxSalary = baseShouldTaxSalary.subtract(new BigDecimal(taxRule.getMarkingPoint()));
        }
        //本月上月个税累计信息对应上月个税累计信息 code
        Map<Integer, Integer> lastTaxOptionCodeMap = new HashMap<>();
        lastTaxOptionCodeMap.put(270101, 250101);
        lastTaxOptionCodeMap.put(270102, 250102);
        lastTaxOptionCodeMap.put(270103, 250103);
        lastTaxOptionCodeMap.put(270106, 250105);
        Map<Integer, String> lastTaxOptionValueMap;
        List<HrmSalaryMonthOptionValue> lastTaxOptionValueList = salaryMonthOptionValueService.lambdaQuery().eq(HrmSalaryMonthOptionValue::getSEmpRecordId, salaryMonthEmpRecord.getSEmpRecordId())
                .in(HrmSalaryMonthOptionValue::getCode, lastTaxOptionCodeMap.values()).list();
        if (CollUtil.isNotEmpty(lastTaxOptionValueList) && cumulativeTaxOfLastMonthData == null){
            //如果是更新薪资,则查询本月生成的上月个税累计信息
            lastTaxOptionValueMap = lastTaxOptionValueList.stream().collect(Collectors.toMap(HrmSalaryMonthOptionValue::getCode, HrmSalaryMonthOptionValue::getValue));
        }else {
            List<HrmSalaryMonthOptionValue> lastTaxOptionValueList1;
            //本月个税累计对应上月个税累计code
            Optional<HrmSalaryMonthEmpRecord> lastSalaryMonthEmpRecordOpt = salaryMonthEmpRecordService.lambdaQuery().eq(HrmSalaryMonthEmpRecord::getYear, salaryMonthEmpRecord.getYear())
                    .eq(HrmSalaryMonthEmpRecord::getMonth, salaryMonthEmpRecord.getMonth() - 1).eq(HrmSalaryMonthEmpRecord::getEmployeeId, salaryMonthEmpRecord.getEmployeeId()).oneOpt();
            //上月个税累计信息
            if (lastSalaryMonthEmpRecordOpt.isPresent()) {
                HrmSalaryMonthEmpRecord lastSalaryMonthEmpRecord = lastSalaryMonthEmpRecordOpt.get();
                lastTaxOptionValueList1 = salaryMonthOptionValueService.lambdaQuery().eq(HrmSalaryMonthOptionValue::getSEmpRecordId, lastSalaryMonthEmpRecord.getSEmpRecordId())
                        .in(HrmSalaryMonthOptionValue::getCode, lastTaxOptionCodeMap.keySet()).list();
                if (CollUtil.isEmpty(lastTaxOptionValueList1)){
                    lastTaxOptionCodeMap.keySet().forEach(code -> {
                        HrmSalaryMonthOptionValue salaryMonthOptionValue = new HrmSalaryMonthOptionValue();
                        salaryMonthOptionValue.setSEmpRecordId(salaryMonthEmpRecord.getSEmpRecordId());
                        salaryMonthOptionValue.setCode(code);
                        salaryMonthOptionValue.setValue("0");
                        lastTaxOptionValueList1.add(salaryMonthOptionValue);
                    });
                }
            } else {
                //查询不到上月,默认填充0
                lastTaxOptionValueList1 = new ArrayList<>();
                lastTaxOptionCodeMap.keySet().forEach(code -> {
                    HrmSalaryMonthOptionValue salaryMonthOptionValue = new HrmSalaryMonthOptionValue();
                    salaryMonthOptionValue.setSEmpRecordId(salaryMonthEmpRecord.getSEmpRecordId());
                    salaryMonthOptionValue.setCode(code);
                    salaryMonthOptionValue.setValue("0");
                    lastTaxOptionValueList1.add(salaryMonthOptionValue);
                });
            }
            lastTaxOptionValueMap = lastTaxOptionValueList1.stream().peek(option-> option.setCode(lastTaxOptionCodeMap.get(option.getCode())))
                    .collect(Collectors.toMap(HrmSalaryMonthOptionValue::getCode, HrmSalaryMonthOptionValue::getValue));
            if (CollUtil.isNotEmpty(cumulativeTaxOfLastMonthData)) {
                //是否导入上月个税累计,导入需覆盖
                cumulativeTaxOfLastMonthData.forEach(lastTaxOptionValueMap::put);
            }
        }
        //个税累计信息
        //累计收入额 同一个纳税年度内，员工在该企业累计至当前月份的收入额（收入额=应发工资-税后补发+其它）code=270101
        BigDecimal cumulativeIncome = new BigDecimal(lastTaxOptionValueMap.get(250101)).add(shouldPaySalary);
        //累计减除费用 同一个纳税年度内，员工截至当前月（计薪月）在本单位的任职受雇从业月份数*5000  code=270102
        BigDecimal cumulativeDeductions = new BigDecimal(lastTaxOptionValueMap.get(250102)).add(new BigDecimal(5000));
        //累计专项扣除 同一个纳税年度内，员工在该企业累计至当前月份的个人社保、个人公积金等费用 code=270103
        BigDecimal cumulativeSpecialDeduction = new BigDecimal(lastTaxOptionValueMap.get(250103)).add(proxyPaySalary);
        //累计专项附加扣除 同一个纳税年度内，员工在该企业累计至当前月份的五项专项附加扣除合计 code=270104
        BigDecimal cumulativeSpecialAdditionalDeduction = taxSpecialGrandTotal;
        //累计应纳税所得额 同一个纳税年度内，员工在该企业累计至当前月份的应税工资 code=270105
        //累计应纳税所得额 = 累计收入-累计免税收入（工资项为加项，且不参与计税的工资）-累计基本减除费用-累计专项扣除-累计专项附加扣除-累计依法确定的其他扣除（标红的先不用考虑），
        BigDecimal cumulativeTaxableIncome = cumulativeIncome.subtract(cumulativeDeductions).subtract(cumulativeSpecialDeduction).subtract(cumulativeSpecialAdditionalDeduction);
        //累计应纳税额 同一个纳税年度内，员工在该企业累计至当前月份的累计应缴个税 code=270106
        TaxEntity taxEntity = taxRateRangeMap.get(cumulativeTaxableIncome);
        BigDecimal cumulativeTaxPayable = cumulativeTaxableIncome.multiply(new BigDecimal(taxEntity.getTaxRate())).divide(new BigDecimal(100), 2, BigDecimal.ROUND_UP)
                .subtract(new BigDecimal(taxEntity.getQuickDeduction())).setScale(2,BigDecimal.ROUND_HALF_UP);;
        //当月个人所得税 = 累计应纳税额 - 累计已预交税额
        BigDecimal payTaxSalary = cumulativeTaxPayable.subtract(new BigDecimal(lastTaxOptionValueMap.get(250105)));
        //实发工资 = 应发工资 - 扣代缴项总金额 - 个人所得税 + 税后补发 - 税后补扣  code=240101
        BigDecimal realPaySalary = shouldPaySalary.subtract(proxyPaySalary).subtract(payTaxSalary).add(taxAfterPaySalary);

        //210101	210	应发工资
        //220101	220	应税工资
        //230101	230	个人所得税
        //240101	240	实发工资
        Map<Integer, String> codeValueMap = new HashMap<>();
        codeValueMap.put(210101, shouldPaySalary.toString());
        codeValueMap.put(220101, shouldTaxSalary.toString());
        codeValueMap.put(230101, payTaxSalary.toString());
        codeValueMap.put(240101, realPaySalary.toString());
        //本月对应上月个税累计信息(parentCode=250)
        lastTaxOptionValueMap.forEach(codeValueMap::put);
        //个税累计信息
        codeValueMap.put(270101, cumulativeIncome.toString());
        codeValueMap.put(270102, cumulativeDeductions.toString());
        codeValueMap.put(270103, cumulativeSpecialDeduction.toString());
        codeValueMap.put(270104, cumulativeSpecialAdditionalDeduction.toString());
        codeValueMap.put(270105, cumulativeTaxableIncome.toString());
        codeValueMap.put(270106, cumulativeTaxPayable.toString());
        salaryMonthOptionValueService.lambdaUpdate().in(HrmSalaryMonthOptionValue::getCode, Arrays.asList(
                210101, 220101, 230101, 240101,
                250101, 250102, 250103, 250105,
                270101, 270102, 270103, 270104, 270105, 270106))
                .eq(HrmSalaryMonthOptionValue::getSEmpRecordId, salaryMonthEmpRecord.getSEmpRecordId()).remove();
        codeValueMap.forEach((code, value) -> {
            HrmSalaryMonthOptionValue salaryMonthOptionValue = new HrmSalaryMonthOptionValue();
            salaryMonthOptionValue.setSEmpRecordId(salaryMonthEmpRecord.getSEmpRecordId());
            salaryMonthOptionValue.setCode(code);
            salaryMonthOptionValue.setValue(value);
            salaryMonthOptionValueList.add(salaryMonthOptionValue);
        });
        return salaryMonthOptionValueList;
    }
}
