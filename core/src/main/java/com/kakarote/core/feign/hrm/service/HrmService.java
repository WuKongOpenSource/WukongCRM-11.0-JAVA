package com.kakarote.core.feign.hrm.service;

import com.kakarote.core.common.Result;
import com.kakarote.core.feign.hrm.entity.HrmEmployee;
import com.kakarote.core.feign.hrm.service.impl.HrmServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Set;

@FeignClient(name = "hrm",contextId = "hrm",fallback = HrmServiceImpl.class)
public interface HrmService {

    @PostMapping("/hrm/queryEmployeeListByIds")
    @ApiOperation("通过员工ids查询")
    Result<Set<HrmEmployee>> queryEmployeeListByIds(@RequestBody List<Integer> employeeIds);

    @PostMapping("/hrmJob/employeeChangeRecords")
    public void employeeChangeRecords();

    @PostMapping("/hrmEmployee/queryIsInHrm")
    @ApiOperation("查询登陆用户是否在人资员工中")
    public Result<Boolean> queryIsInHrm();


}
