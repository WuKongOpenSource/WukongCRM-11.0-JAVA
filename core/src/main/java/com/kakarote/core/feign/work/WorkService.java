package com.kakarote.core.feign.work;

import com.kakarote.core.common.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "work",contextId = "taskJob")
public interface WorkService {

    @PostMapping("/workTask/updateTaskJob")
    Result updateTaskJob(@RequestParam("host") String host);

}
