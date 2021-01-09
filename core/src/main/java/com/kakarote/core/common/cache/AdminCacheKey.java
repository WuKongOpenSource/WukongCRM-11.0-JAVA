package com.kakarote.core.common.cache;

/**
 * admin缓存key
 * @author hmb
 */
public interface AdminCacheKey {

    String USER_AUTH_CACHE_KET = "user_auth:";

    /**
     * 导入消息缓存key
     */
    String UPLOAD_EXCEL_MESSAGE_PREFIX = "upload:excel:message:";

    /**
     * 发送短信缓存key
     * &s: host
     */
    String SMS_CACHE_KEY = "send:sms:";


    String PASSWORD_ERROR_CACHE_KEY = "password:error:";

    String TEMPORARY_ACCESS_CODE_CACHE_KEY  = "temporary:access:code:";
}
