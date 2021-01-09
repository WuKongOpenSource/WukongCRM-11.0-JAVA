package com.kakarote.admin.common.log;

import com.kakarote.admin.service.IAdminDeptService;
import com.kakarote.core.common.log.BehaviorEnum;
import com.kakarote.core.common.log.Content;
import com.kakarote.core.servlet.ApplicationContextHolder;

public class AdminDeptLog {
    private IAdminDeptService adminDeptService = ApplicationContextHolder.getBean(IAdminDeptService.class);

    public Content deleteDept(Integer deptId) {
        String deptName = adminDeptService.getNameByDeptId(deptId);
        return new Content(deptName,"删除了部门:"+deptName, BehaviorEnum.DELETE);
    }
}
