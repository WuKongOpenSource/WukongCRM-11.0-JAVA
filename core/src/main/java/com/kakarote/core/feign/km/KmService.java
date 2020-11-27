package com.kakarote.core.feign.km;

import com.kakarote.core.common.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author JiaS
 * @date 2020/11/18
 */
@FeignClient(name = "km",contextId = "knowledgeLibrary")
public interface KmService {

    @PostMapping("/kmKnowledgeLibrary/initKmData")
    Result<Boolean> initKmData();
}
