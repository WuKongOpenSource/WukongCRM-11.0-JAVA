package com.kakarote.admin.common;

import com.kakarote.core.common.Const;

/**
 * @author zhangzhiwei
 * 系统管理模块的常量
 */
public class AdminConst extends Const {
    /**
     * 默认的权限缓存KEY
     */
    public static final String DEFAULT_AUTH_CACHE_NAME = "CRM:AUTH";

    /**
     * 默认的密码强度正则
     */
    public static final String DEFAULT_PASSWORD_INTENSITY = "^(?=.*[a-zA-Z])(?=.*\\d).{6,20}$";

    /**
     * 用户加密公钥
     */
    public static final String userPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkqKFcAQtIp4rlkB5LOMnViyVY/hhA6x0R9ftwtEXsAFu4hBZrm9txdEvxSrDCUsx3Zwv/gdimeOzTtfSKffdoE/DwllNP9Zu6nsr2kGRgPrRwjtlO+j2FOM0b9UY1SQ/bWE+a9oQL2jL9xMSbtX1xG/+HcMo1bT+pa6FNQzs3egmvMt75/jaxINPSraj4kgNFawSBk7qDBEqDYiQwtPTuaNW1YZIs++/gZHsCRgGs/JrAbxNpl7+v/+Z503I3I2rs/8eUM5d16NXR8M7vtobUDCTIiQOgRahO8WMadgFlwavyVCYhy/TBXyj5RUfWaS26LrEN3vkj4TjoJu5m9LQ5QIDAQAB";
}
