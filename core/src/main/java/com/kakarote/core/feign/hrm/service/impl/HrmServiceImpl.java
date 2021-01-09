package com.kakarote.core.feign.hrm.service.impl;

import com.kakarote.core.common.Result;
import com.kakarote.core.feign.hrm.entity.HrmEmployee;
import com.kakarote.core.feign.hrm.service.HrmService;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class HrmServiceImpl implements HrmService {

    @Override
    public Result<Set<HrmEmployee>> queryEmployeeListByIds(List<Integer> employeeIds) {
        return Result.ok(new HashSet<>());
    }

    @Override
    public void employeeChangeRecords() {

    }

    @Override
    public Result<Boolean> queryIsInHrm() {
        return Result.ok(false);
    }
}
