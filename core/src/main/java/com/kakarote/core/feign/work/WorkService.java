package com.kakarote.core.feign.work;

import com.kakarote.core.common.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "work",contextId = "taskJob")
public interface WorkService {

    @PostMapping("/workTask/updateTaskJob")
    Result updateTaskJob();


    @PostMapping("/work/initWorkData")
    Result<Boolean> initWorkData();

}
