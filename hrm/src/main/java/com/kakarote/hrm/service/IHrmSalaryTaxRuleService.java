package com.kakarote.hrm.service;

import com.kakarote.core.servlet.BaseService;
import com.kakarote.hrm.entity.BO.SetTaxRuleBO;
import com.kakarote.hrm.entity.PO.HrmSalaryTaxRule;

import java.util.List;

/**
 * <p>
 * 计税规则 服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-26
 */
public interface IHrmSalaryTaxRuleService extends BaseService<HrmSalaryTaxRule> {

    /**
     * 查询计税规则列表
     * @return
     */
    List<HrmSalaryTaxRule> queryTaxRuleList();

    /**
     * 修改计规则
     * @param setTaxRuleBO
     */
    void setTaxRule(SetTaxRuleBO setTaxRuleBO);

}
