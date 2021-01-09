package com.kakarote.core.common.cache;

/**
 * 人力资源缓存key
 * @author hmb
 */
public interface HrmCacheKey {

    /**
     * 社保类型缓存key
     */
    String INSURANCE_TYPE_CACHE_KEY = "HRM:INSURANCE_TYPE:";
    /**
     * 社保比例缓存key
     */
    String INSURANCE_SCALE_CACHE_KEY = "HRM:INSURANCE_SCALE:";
    /**
     * 员工手机号缓存key
     */
    String HRM_EMPLOYEE_MOBILE_CACHE = "HRM:EMPLOYEE:MOBILE:CACHE:";

    /**
     * 员工用户id缓存key
     */
    String HRM_EMPLOYEE_USER_CACHE = "HRM:EMPLOYEE:USER:CACHE:";
    /**
     * 人资日历缓存key
     */
    String HRM_NOTES_CACHE_KEY = "HRM:NOTES:%s:%s-%s";
}
