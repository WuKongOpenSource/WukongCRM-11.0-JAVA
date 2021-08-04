package com.kakarote.hrm.mapper;

import com.kakarote.core.servlet.BaseMapper;
import com.kakarote.hrm.entity.DTO.ComputeSalaryDTO;
import com.kakarote.hrm.entity.PO.HrmSalaryMonthOptionValue;

import java.util.List;

/**
 * <p>
 * 每月员工薪资项表 Mapper 接口
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-26
 */
public interface HrmSalaryMonthOptionValueMapper extends BaseMapper<HrmSalaryMonthOptionValue> {

    List<ComputeSalaryDTO> querySalaryOptionValue(Integer sEmpRecordId);
}
