package com.kakarote.hrm.service.impl;

import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.hrm.entity.PO.HrmInsuranceMonthEmpProjectRecord;
import com.kakarote.hrm.mapper.HrmInsuranceMonthEmpProjectRecordMapper;
import com.kakarote.hrm.service.IHrmInsuranceMonthEmpProjectRecordService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * 员工每月参保项目表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-26
 */
@Service
public class HrmInsuranceMonthEmpProjectRecordServiceImpl extends BaseServiceImpl<HrmInsuranceMonthEmpProjectRecordMapper, HrmInsuranceMonthEmpProjectRecord> implements IHrmInsuranceMonthEmpProjectRecordService {

    @Override
    public Map<String, Object> queryProjectCount(Integer iEmpRecordId) {
        return getBaseMapper().queryProjectCount(iEmpRecordId);
    }
}
