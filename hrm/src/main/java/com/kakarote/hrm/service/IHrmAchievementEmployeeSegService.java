package com.kakarote.hrm.service;

import com.kakarote.core.servlet.BaseService;
import com.kakarote.hrm.entity.PO.HrmAchievementEmployeeSeg;

import java.util.List;

/**
 * <p>
 * 员工绩效考核项 服务类
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
public interface IHrmAchievementEmployeeSegService extends BaseService<HrmAchievementEmployeeSeg> {
    /**
     * 查询员工考核项
     * @param employeeAppraisalId
     * @return
     */
    List<HrmAchievementEmployeeSeg> queryAppraisalSeg(Integer employeeAppraisalId);
}
