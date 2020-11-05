package com.kakarote.job.work;

import com.kakarote.core.feign.work.WorkService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskJob {


    @Autowired
    private WorkService workService;

    @XxlJob("TaskJob")
    public ReturnT<String> TaskJobHandler(String param){
        workService.updateTaskJob();
        return ReturnT.SUCCESS;
    }
}
