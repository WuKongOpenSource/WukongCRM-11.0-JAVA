package com.kakarote.core.feign.admin.service;

import com.kakarote.core.common.Result;
import com.kakarote.core.common.log.SysLogEntity;
import com.kakarote.core.feign.admin.entity.LoginLogEntity;
import com.kakarote.core.feign.admin.service.impl.LogServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author hmb
 */
@FeignClient(name = "admin",contextId = "log" ,fallback = LogServiceImpl.class)
@Component
public interface LogService {

    @PostMapping("/adminSysLog/saveSysLog")
    Result saveSysLog(@RequestBody SysLogEntity sysLogEntity);

    @PostMapping("/adminSysLog/saveLoginLog")
    Result saveLoginLog(@RequestBody LoginLogEntity loginLogEntity);
}
