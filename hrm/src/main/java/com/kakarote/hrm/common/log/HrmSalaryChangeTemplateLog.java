package com.kakarote.hrm.common.log;

import com.kakarote.core.common.log.BehaviorEnum;
import com.kakarote.core.common.log.Content;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.hrm.entity.PO.HrmSalaryChangeTemplate;
import com.kakarote.hrm.service.IHrmSalaryChangeTemplateService;

public class HrmSalaryChangeTemplateLog {

    private IHrmSalaryChangeTemplateService salaryChangeTemplateService = ApplicationContextHolder.getBean(IHrmSalaryChangeTemplateService.class);

    public Content deleteChangeTemplate(Integer id){
        HrmSalaryChangeTemplate template = salaryChangeTemplateService.getById(id);
        salaryChangeTemplateService.deleteChangeTemplate(id);
        return new Content(template.getTemplateName(),"删除了模板"+template.getTemplateName(), BehaviorEnum.DELETE);
    }
}
