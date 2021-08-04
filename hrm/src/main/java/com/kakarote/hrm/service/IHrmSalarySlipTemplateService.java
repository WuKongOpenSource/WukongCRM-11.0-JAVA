package com.kakarote.hrm.service;

import com.kakarote.core.servlet.BaseService;
import com.kakarote.hrm.entity.BO.AddSlipTemplateBO;
import com.kakarote.hrm.entity.PO.HrmSalarySlipTemplate;

/**
 * <p>
 * 工资表模板 服务类
 * </p>
 *
 * @author hmb
 * @since 2020-11-03
 */
public interface IHrmSalarySlipTemplateService extends BaseService<HrmSalarySlipTemplate> {

    /**
     * 添加工资条模板
     * @param addSlipTemplateBO
     */
    void addSlipTemplate(AddSlipTemplateBO addSlipTemplateBO);

    /**
     * 删除工资条模板
     * @param templateId
     */
    void deleteSlipTemplate(Integer templateId);
}
