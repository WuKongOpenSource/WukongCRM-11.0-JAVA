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
     * 初始化数据
     * @date 2020/9/16 13:45
     * @return
     **/
    @PostMapping("/crmAnalysis/initCrmData")
    Result<Boolean> initCrmData();

}
