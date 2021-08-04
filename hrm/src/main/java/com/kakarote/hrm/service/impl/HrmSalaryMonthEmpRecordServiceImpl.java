package com.kakarote.hrm.service.impl;

import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.hrm.entity.PO.HrmSalaryMonthEmpRecord;
import com.kakarote.hrm.mapper.HrmSalaryMonthEmpRecordMapper;
import com.kakarote.hrm.service.IHrmSalaryMonthEmpRecordService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 员工每月薪资记录 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-26
 */
@Service
public class HrmSalaryMonthEmpRecordServiceImpl extends BaseServiceImpl<HrmSalaryMonthEmpRecordMapper, HrmSalaryMonthEmpRecord> implements IHrmSalaryMonthEmpRecordService {

    @Override
    public List<Integer> queryEmployeeIds(Integer sRecordId, Collection<Integer> dataAuthEmployeeIds) {
        return getBaseMapper().queryEmployeeIds(sRecordId,dataAuthEmployeeIds);
    }
}
