package com.kakarote.hrm.common;

import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.feign.admin.entity.AdminRole;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.hrm.entity.VO.EmployeeInfo;
import com.kakarote.hrm.service.IHrmEmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

/**
 * @author zhangzhiwei
 * user注入切面
 */
@Aspect
@Component
@Slf4j
@Order(10)
public class EmployeeAspect{

    @Autowired
    private IHrmEmployeeService employeeService;

    @Autowired
    private AdminService adminService;

    @Around("execution(* com.kakarote.hrm.controller..*.*(..)) && !execution(@(com.kakarote.core.common.ParamAspect) * *(..)) ")
    public Object before(ProceedingJoinPoint point) throws Throwable {
        try {
            UserInfo user = UserUtil.getUser();
            EmployeeInfo employeeInfo = employeeService.queryEmployeeInfoByMobile(user.getUsername());
            if (employeeInfo == null){
                employeeInfo = new EmployeeInfo();
            }
            AdminRole adminRole;
            if (UserUtil.isAdmin()){
                adminRole = new AdminRole();
                adminRole.setLabel(91);
            }else {
                List<AdminRole> roles = adminService.queryRoleByRoleTypeAndUserId(9).getData();
                adminRole = roles.stream().min(Comparator.comparingInt(AdminRole::getLabel)).orElse(null);
            }
            employeeInfo.setRole(adminRole);
            EmployeeHolder.setEmployeeInfo(employeeInfo);
            return point.proceed();
        } finally {
            EmployeeHolder.remove();
        }
    }
}
