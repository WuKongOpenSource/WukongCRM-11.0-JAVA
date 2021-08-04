package com.kakarote.hrm.service;

import com.kakarote.core.servlet.BaseService;
import com.kakarote.hrm.entity.DTO.ComputeSalaryDTO;
import com.kakarote.hrm.entity.PO.HrmSalaryMonthOptionValue;

import java.util.List;

/**
 * <p>
 * 每月员工薪资项表 服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-26
 */
public interface IHrmSalaryMonthOptionValueService extends BaseService<HrmSalaryMonthOptionValue> {

    /**
     * 查询计算的薪资项
     * @param sEmpRecordId
     */
    List<ComputeSalaryDTO> querySalaryOptionValue(Integer sEmpRecordId);
}
