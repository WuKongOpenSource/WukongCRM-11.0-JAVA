package com.kakarote.core.feign.crm.service;

import com.kakarote.core.common.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author JiaS
 * @date 2020/9/16
 */
@FeignClient(name = "crm",contextId = "analysis")
public interface CrmAnalysisService {


    /**
    * 统计客户信息
    * @date 2020/9/16 13:45
    * @param host
    * @return
    **/
    @PostMapping("/crmAnalysis/customerStats")
    Result<Boolean> customerStats();



}
