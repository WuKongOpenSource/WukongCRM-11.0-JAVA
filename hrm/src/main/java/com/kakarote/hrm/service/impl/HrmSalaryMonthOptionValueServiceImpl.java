package com.kakarote.hrm.service.impl;

import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.hrm.entity.DTO.ComputeSalaryDTO;
import com.kakarote.hrm.entity.PO.HrmSalaryMonthOptionValue;
import com.kakarote.hrm.mapper.HrmSalaryMonthOptionValueMapper;
import com.kakarote.hrm.service.IHrmSalaryMonthOptionValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 每月员工薪资项表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-26
 */
@Service
public class HrmSalaryMonthOptionValueServiceImpl extends BaseServiceImpl<HrmSalaryMonthOptionValueMapper, HrmSalaryMonthOptionValue> implements IHrmSalaryMonthOptionValueService {

    @Autowired
    private HrmSalaryMonthOptionValueMapper optionValueMapper;

    @Override
    public List<ComputeSalaryDTO> querySalaryOptionValue(Integer sEmpRecordId) {
        return optionValueMapper.querySalaryOptionValue(sEmpRecordId);
    }
}
