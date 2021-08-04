package com.kakarote.hrm.service;

import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;
import com.kakarote.hrm.entity.BO.*;
import com.kakarote.hrm.entity.PO.HrmAchievementEmployeeAppraisal;
import com.kakarote.hrm.entity.VO.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 员工绩效考核 服务类
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
public interface IHrmAchievementEmployeeAppraisalService extends BaseService<HrmAchievementEmployeeAppraisal> {

    /**
     * 查询员工绩效数量
     * @return
     */
    Map<Integer, Integer> queryAppraisalNum();

    /**
     * 查询我的绩效
     * @param basePageBO
     * @return
     */
    BasePage<QueryMyAppraisalVO> queryMyAppraisal(BasePageBO basePageBO);

    /**
     * 查询目标确认列表
     * @param basePageBO
     * @return
     */
    BasePage<TargetConfirmListVO> queryTargetConfirmList(BasePageBO basePageBO);

    /**
     * 查询结果评定列表
     * @param evaluatoListBO
     * @return
     */
    BasePage<EvaluatoListVO> queryEvaluatoList(EvaluatoListBO evaluatoListBO);

    /**
     * 查询考核详情
     * @param employeeAppraisalId
     * @return
     */
    EmployeeAppraisalDetailVO queryEmployeeAppraisalDetail(Integer employeeAppraisalId);

    /**
     * 填写绩效
     * @param writeAppraisalBO
     */
    void writeAppraisal(WriteAppraisalBO writeAppraisalBO);

    /**
     * 目标确认
     * @param targetConfirmBO
     */
    void targetConfirm(TargetConfirmBO targetConfirmBO);

    /**
     * 结果评定
     * @param resultEvaluatoBO
     */
    void resultEvaluato(ResultEvaluatoBO resultEvaluatoBO);

    /**
     * 查询结果确认列表
     * @param basePageBO
     * @return
     */
    BasePage<ResultConfirmListVO> queryResultConfirmList(BasePageBO basePageBO);

    /**
     * 绩效结果确认
     * @param appraisalId
     * @return
     */
    ResultConfirmByAppraisalIdVO queryResultConfirmByAppraisalId(String appraisalId);

    /**
     * 修改考评分数
     * @param updateScoreLevelBO
     */
    void updateScoreLevel(UpdateScoreLevelBO updateScoreLevelBO);

    /**
     * 结果确认
     * @param appraisalId
     */
    void resultConfirm(String appraisalId);

    /**
     * 修改目标进度
     * @param updateScheduleBO
     */
    void updateSchedule(UpdateScheduleBO updateScheduleBO);
    /**
     * 查询目标确认列表的绩效筛选条件
     * @return
     */

    List<AchievementAppraisalVO> queryTargetConfirmScreen(Integer employeeId, Integer status);

    /**
     * 查询结果评定列表的绩效筛选条件
     * @return
     */
    List<AchievementAppraisalVO> queryEvaluatoScreen(Integer employeeId,Integer status);

    /**
     * 查询等级id通过评分
     * @param queryLevelIdByScoreBO
     * @return
     */
    Integer queryLevelIdByScore(QueryLevelIdByScoreBO queryLevelIdByScoreBO);


}
