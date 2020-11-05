package com.kakarote.core.common.cache;

/**
 * crm缓存key
 * @author hmb
 */
public interface CrmCacheKey {
    /**
     * 打印模板缓存key
     */
    String CRM_PRINT_TEMPLATE_CACHE_KEY = "CRM:PRINT:TEMPLATE:";

    /**
     * 定时放入公海缓存key
     */
    String CRM_CUSTOMER_JOB_CACHE_KEY = "CrmCustomerJob:";

    /**
     * crm待办事项数量缓存key
     */
    String CRM_BACKLOG_NUM_CACHE_KEY = "queryBackLogNum:";

    /**
     * crm呼叫中心缓存key
     */
    String CRM_CALL_CACHE_KEY = "call:";
}
