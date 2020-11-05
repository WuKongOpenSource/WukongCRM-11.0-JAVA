package com.kakarote.job.oa;

import com.kakarote.core.common.cache.OaCacheKey;
import com.kakarote.core.feign.oa.OaService;
import com.kakarote.core.redis.Redis;
import com.kakarote.core.utils.UserUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventNoticeJob {

    @Autowired
    private OaService oaService;

    @Autowired
    private Redis redis;


    @XxlJob("EventNoticeJob")
    public ReturnT<String> EventNoticeJobHandler(String param) {
        try {
            oaService.eventNoticeCron();
        } finally {
            redis.del(OaCacheKey.EVENT_NOTICE_JOB_KEY);
            UserUtil.removeUser();
        }

        return ReturnT.SUCCESS;
    }
}
