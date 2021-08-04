package com.kakarote.hrm.common.log;

import com.kakarote.core.common.log.BehaviorEnum;
import com.kakarote.core.common.log.Content;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.hrm.constant.LabelGroupEnum;
import com.kakarote.hrm.entity.BO.AddEmployeeFieldBO;
import com.kakarote.hrm.entity.PO.HrmInsuranceScheme;
import com.kakarote.hrm.service.IHrmInsuranceSchemeService;

public class HrmConfigLog {

    private IHrmInsuranceSchemeService insuranceSchemeService = ApplicationContextHolder.getBean(IHrmInsuranceSchemeService.class);

    public Content saveField(AddEmployeeFieldBO addEmployeeFieldBO) {
        String name = LabelGroupEnum.parse(addEmployeeFieldBO.getLabelGroup()).getName();
        return new Content(name,"修改了自定义字段:"+name, BehaviorEnum.UPDATE);
    }

    public Content deleteInsuranceScheme(Integer schemeId) {
        HrmInsuranceScheme insuranceScheme = insuranceSchemeService.getById(schemeId);
        return new Content(insuranceScheme.getSchemeName(),"删除了社保方案:"+insuranceScheme.getSchemeName(), BehaviorEnum.DELETE);
    }

}
