package com.kakarote.admin.common.log;

import com.kakarote.admin.entity.PO.AdminRole;
import com.kakarote.admin.service.IAdminRoleService;
import com.kakarote.core.common.log.BehaviorEnum;
import com.kakarote.core.common.log.Content;
import com.kakarote.core.servlet.ApplicationContextHolder;

public class AdminRoleLog {
    private IAdminRoleService adminRoleService = ApplicationContextHolder.getBean(IAdminRoleService.class);

    public Content delete(Integer roleId) {
        AdminRole adminRole = adminRoleService.getById(roleId);
        return new Content(adminRole.getRoleName(),"删除了角色:"+adminRole.getRoleName(), BehaviorEnum.DELETE);
    }

    public Content copy(Integer roleId) {
        AdminRole adminRole = adminRoleService.getById(roleId);
        return new Content(adminRole.getRoleName(),"复制了角色:"+adminRole.getRoleName(), BehaviorEnum.COPY);
    }

    public Content deleteWorkRole(Integer roleId) {
        AdminRole adminRole = adminRoleService.getById(roleId);
        return new Content(adminRole.getRoleName(),"删除了项目角色:"+adminRole.getRoleName(), BehaviorEnum.DELETE);
    }
}
