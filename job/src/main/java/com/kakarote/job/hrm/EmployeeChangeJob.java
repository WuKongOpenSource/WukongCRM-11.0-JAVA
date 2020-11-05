package com.kakarote.job.hrm;

import com.kakarote.core.feign.hrm.service.HrmService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmployeeChangeJob {

    @Autowired
    private HrmService hrmService;

    @XxlJob("EmployeeChangeJob")
    public ReturnT<String> employeeChangeJobHandler(String param) {
        hrmService.employeeChangeRecords();
        return ReturnT.SUCCESS;
    }
}
