package com.kakarote.job.bi;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

/**
 * @author JiaS
 * @date 2020/9/16
 */
@Component
public class BiCustomerStatsJob {

    @XxlJob("BiCustomerStatsJob")
    public ReturnT<String> biCustomerStatsJobHandler(String param) {
        return ReturnT.SUCCESS;
    }

}
