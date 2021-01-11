package com.kakarote.job.bi;

import com.kakarote.core.feign.crm.service.CrmAnalysisService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author JiaS
 * @date 2020/9/16
 */
@Component
public class BiCustomerStatsJob {

    @Autowired
    private CrmAnalysisService crmAnalysisService;

    @XxlJob("BiCustomerStatsJob")
    public ReturnT<String> biCustomerStatsJobHandler(String param) {
        return ReturnT.SUCCESS;
    }

}
