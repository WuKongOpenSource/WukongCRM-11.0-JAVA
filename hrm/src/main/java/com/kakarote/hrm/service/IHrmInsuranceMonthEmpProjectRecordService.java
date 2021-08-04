package com.kakarote.hrm.service;

import com.kakarote.core.servlet.BaseService;
import com.kakarote.hrm.entity.PO.HrmInsuranceMonthEmpProjectRecord;

import java.util.Map;

/**
 * <p>
 * 员工每月参保项目表 服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-26
 */
public interface IHrmInsuranceMonthEmpProjectRecordService extends BaseService<HrmInsuranceMonthEmpProjectRecord> {

    /**
     * 查询社保项统计
     * @param iEmpRecordId
     * @return
     */
    Map<String, Object> queryProjectCount(Integer iEmpRecordId);
}
