package com.kakarote.core.common.cache;

/**
 * admin缓存key
 * @author hmb
 */
public interface AdminCacheKey {
    /**
     * 导入消息缓存key
     */
    String UPLOAD_EXCEL_MESSAGE_PREFIX = "upload:excel:message:";

    /**
     * 发送短信缓存key
     * &s: host
     */
    String SMS_CACHE_KEY = "send:sms:";
}
