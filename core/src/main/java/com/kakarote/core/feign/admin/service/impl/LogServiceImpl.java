package com.kakarote.core.feign.admin.service.impl;

import com.kakarote.core.common.Result;
import com.kakarote.core.common.log.SysLogEntity;
import com.kakarote.core.feign.admin.entity.LoginLogEntity;
import com.kakarote.core.feign.admin.service.LogService;
import org.springframework.stereotype.Component;

/**
 * @author hmb
 */
@Component
public class LogServiceImpl implements LogService {


    @Override
    public Result saveSysLog(SysLogEntity sysLogEntity) {
        return Result.ok();
    }

    @Override
    public Result saveLoginLog(LoginLogEntity loginLogEntity) {
        return Result.ok();
    }
}
