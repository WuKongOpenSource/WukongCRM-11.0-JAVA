package com.kakarote.hrm.utils;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.kakarote.core.common.cache.HrmCacheKey;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.hrm.entity.PO.HrmEmployee;
import com.kakarote.hrm.service.IHrmEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangzhiwei
 * 用户缓存相关方法
 */
@Component
public class EmployeeCacheUtil {
    static EmployeeCacheUtil ME;

    @PostConstruct
    public void init() {
        ME = this;
    }

    @CreateCache(name = HrmCacheKey.HRM_EMPLOYEE_USER_CACHE, expire = 3, timeUnit = TimeUnit.DAYS)
    private Cache<Integer, Long> employeeUserIdCache;

    @CreateCache(name = HrmCacheKey.HRM_EMPLOYEE_MOBILE_CACHE, expire = 3, timeUnit = TimeUnit.DAYS)
    private Cache<Integer, String> employeeMobileCache;

    @Autowired
    private AdminService adminService;

    @Autowired
    private IHrmEmployeeService employeeService;
    /**
     * 根据员工ID获取系统用户id
     *
     * @param employeeId 员工id
     * @return data
     */
    public static Long getUserId(Integer employeeId) {
        if (employeeId == null) {
            return 0L;
        }
        Integer key = employeeId;
        Long userId = ME.employeeUserIdCache.get(key);
        if (userId == null) {
            String employeeMobile = getEmployeeMobile(employeeId);
            userId = ME.adminService.queryUserIdByUserName(employeeMobile).getData();
            ME.employeeUserIdCache.put(key, userId);
        }
        return userId;
    }

    /**
     * 根据员工ID获取系统用户id
     *
     * @param employeeId 员工id
     * @return data
     */
    public static String getEmployeeMobile(Integer employeeId) {
        if (employeeId == null) {
            return "";
        }
        Integer key = employeeId;
        String mobile = ME.employeeMobileCache.get(key);
        if (mobile == null) {
            mobile = ME.employeeService.lambdaQuery().select(HrmEmployee::getMobile).eq(HrmEmployee::getEmployeeId,employeeId).one().getMobile();
            ME.employeeMobileCache.put(key, mobile);
        }
        return mobile;
    }


}
