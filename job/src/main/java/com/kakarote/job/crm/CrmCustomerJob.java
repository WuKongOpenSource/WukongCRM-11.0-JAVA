package com.kakarote.job.crm;

import com.kakarote.core.common.Const;
import com.kakarote.core.common.Result;
import com.kakarote.core.common.cache.CrmCacheKey;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.feign.crm.service.CrmService;
import com.kakarote.core.redis.Redis;
import com.kakarote.core.utils.UserUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 客户定时放入公海
 */
@Component
@Slf4j
public class CrmCustomerJob {

    @Autowired
    private CrmService crmService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private Redis redis;

    @XxlJob("CrmCustomerJob")
    public ReturnT<String> crmCustomerJobHandler(String param) {
            try {
                Long userId = UserUtil.getSuperUser();
                if (userId != null) {
                    UserInfo userInfo = UserUtil.setUser(userId);
                    redis.setex(CrmCacheKey.CRM_CUSTOMER_JOB_CACHE_KEY, Const.MAX_USER_EXIST_TIME, userInfo);
                    Result result = crmService.putInInternational();
                    if (!result.hasSuccess()){
                        ReturnT<String> fail = ReturnT.FAIL;
                        fail.setMsg(result.getMsg());
                        return fail;
                    }
                }
            }finally {
                redis.del(CrmCacheKey.CRM_CUSTOMER_JOB_CACHE_KEY);
                UserUtil.removeUser();
            }

        XxlJobLogger.log("客户定时放入公海执行完成");
        return ReturnT.SUCCESS;
    }
}
