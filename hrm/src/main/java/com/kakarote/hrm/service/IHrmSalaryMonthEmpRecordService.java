package com.kakarote.hrm.service;

import com.kakarote.core.servlet.BaseService;
import com.kakarote.hrm.entity.PO.HrmSalaryMonthEmpRecord;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 员工每月薪资记录 服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-26
 */
public interface IHrmSalaryMonthEmpRecordService extends BaseService<HrmSalaryMonthEmpRecord> {


    /**
     * 查询薪资员工id
     * @param sRecordId
     * @param dataAuthEmployeeIds
     * @return
     */
    List<Integer> queryEmployeeIds(Integer sRecordId, Collection<Integer> dataAuthEmployeeIds);
}
