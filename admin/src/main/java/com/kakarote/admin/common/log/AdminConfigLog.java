package com.kakarote.admin.common.log;

import com.kakarote.admin.entity.BO.ModuleSettingBO;
import com.kakarote.admin.entity.PO.AdminConfig;
import com.kakarote.admin.service.IAdminConfigService;
import com.kakarote.core.common.log.BehaviorEnum;
import com.kakarote.core.common.log.Content;
import com.kakarote.core.servlet.ApplicationContextHolder;

public class AdminConfigLog {

    private IAdminConfigService adminConfigService = ApplicationContextHolder.getBean(IAdminConfigService.class);

    public Content setModuleSetting(ModuleSettingBO moduleSetting) {
        AdminConfig adminConfig = adminConfigService.getById(moduleSetting.getSettingId());
        String detail;
        if (moduleSetting.getStatus()== 0){
            detail = "停用了";
        }else {
            detail = "启用了";
        }
        detail += adminConfig.getName();
        return new Content(adminConfig.getName(),detail, BehaviorEnum.UPDATE);
    }
}
