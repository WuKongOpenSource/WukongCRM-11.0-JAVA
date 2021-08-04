package com.kakarote.hrm.mapper;

import com.kakarote.core.servlet.BaseMapper;
import com.kakarote.hrm.entity.PO.HrmInsuranceMonthEmpProjectRecord;

import java.util.Map;

/**
 * <p>
 * 员工每月参保项目表 Mapper 接口
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-26
 */
public interface HrmInsuranceMonthEmpProjectRecordMapper extends BaseMapper<HrmInsuranceMonthEmpProjectRecord> {

    Map<String, Object> queryProjectCount(Integer iEmpRecordId);
}
