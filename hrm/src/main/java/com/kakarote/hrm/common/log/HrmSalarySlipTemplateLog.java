package com.kakarote.hrm.common.log;

import com.kakarote.core.common.log.BehaviorEnum;
import com.kakarote.core.common.log.Content;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.hrm.entity.PO.HrmSalarySlipTemplate;
import com.kakarote.hrm.service.IHrmSalarySlipTemplateService;
import org.springframework.web.bind.annotation.PathVariable;

public class HrmSalarySlipTemplateLog {

    private IHrmSalarySlipTemplateService salarySlipTemplateService = ApplicationContextHolder.getBean(IHrmSalarySlipTemplateService.class);

    public Content deleteSlipTemplate(@PathVariable Integer templateId){
        HrmSalarySlipTemplate template = salarySlipTemplateService.getById(templateId);
        return new Content(template.getTemplateName(),"删除了"+template.getTemplateName(), BehaviorEnum.DELETE);
    }
}
