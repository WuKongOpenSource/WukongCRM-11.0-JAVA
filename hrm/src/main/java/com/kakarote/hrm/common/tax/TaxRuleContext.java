package com.kakarote.hrm.common.tax;

import com.kakarote.hrm.constant.TaxType;
import com.kakarote.hrm.entity.PO.HrmSalaryMonthEmpRecord;
import com.kakarote.hrm.entity.PO.HrmSalaryMonthOptionValue;
import com.kakarote.hrm.entity.PO.HrmSalaryTaxRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 创建核算薪资Context:
 * @author hmb
 */
@Component
public class TaxRuleContext {

    @Autowired
    private final Map<String, AbstractTaxRuleStrategy> strategyMap = new  ConcurrentHashMap<>();

    public List<HrmSalaryMonthOptionValue> getTaxRuleStrategyOptionValue(HrmSalaryMonthEmpRecord salaryMonthEmpRecord, HrmSalaryTaxRule taxRule,
                                                                          Map<Integer, String> cumulativeTaxOfLastMonthData) {
        return strategyMap.get(TaxType.parse(taxRule.getTaxType()).name()).baseComputeSalary(salaryMonthEmpRecord)
                .computeSalary(salaryMonthEmpRecord,taxRule,cumulativeTaxOfLastMonthData);
    }

}
