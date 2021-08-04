package com.kakarote.hrm.service;

import com.kakarote.core.servlet.BaseService;
import com.kakarote.hrm.entity.PO.HrmAchievementAppraisalScoreLevel;

import java.util.List;

/**
 * <p>
 * 考评规则等级 服务类
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
public interface IHrmAchievementAppraisalScoreLevelService extends BaseService<HrmAchievementAppraisalScoreLevel> {


    /**
     * 使用员工绩效id查询绩效分数列表
     * @param employeeAppraisalId 员工绩效id
     */
    List<HrmAchievementAppraisalScoreLevel> queryScoreLevelList(Integer employeeAppraisalId);

    /**
     * 使用绩效id查询绩效分数列表
     * @param appraisalId 绩效id
     */
    List<HrmAchievementAppraisalScoreLevel> queryScoreLevelListByAppraisalId(Integer appraisalId);
}
