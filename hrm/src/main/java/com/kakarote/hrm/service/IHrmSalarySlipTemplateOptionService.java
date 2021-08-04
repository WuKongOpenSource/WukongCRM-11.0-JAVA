package com.kakarote.hrm.service;

import com.kakarote.core.servlet.BaseService;
import com.kakarote.hrm.entity.PO.HrmSalarySlipTemplateOption;

import java.util.List;

/**
 * <p>
 * 工资条模板项 服务类
 * </p>
 *
 * @author hmb
 * @since 2020-11-03
 */
public interface IHrmSalarySlipTemplateOptionService extends BaseService<HrmSalarySlipTemplateOption> {

    /**
     * 查询工资模板项
     * @param templateId
     * @return
     */
    List<HrmSalarySlipTemplateOption> queryTemplateOptionByTemplateId(Integer templateId);
}
