package com.kakarote.hrm.service;

import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;
import com.kakarote.hrm.entity.BO.*;
import com.kakarote.hrm.entity.PO.HrmAchievementAppraisal;
import com.kakarote.hrm.entity.PO.HrmAchievementEmployeeAppraisal;
import com.kakarote.hrm.entity.VO.*;

import java.util.Map;

/**
 * <p>
 * 绩效考核 服务类
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
public interface IHrmAchievementAppraisalService extends BaseService<HrmAchievementAppraisal> {

    /**
     * 添加绩效
     * @param setAppraisalBO 绩效对象
     */
    void addAppraisal(SetAppraisalBO setAppraisalBO);

    /**
     * 修改绩效
     * @param setAppraisalBO 绩效对象
     */
    void setAppraisal(SetAppraisalBO setAppraisalBO);

    /**
     * 删除绩效
     * @param appraisalId 绩效id
     */
    void deleteAppraisal(Integer appraisalId);

    /**
     * 停止绩效
     * @param appraisalId 绩效id
     */
    void stopAppraisal(Integer appraisalId);


    /**
     * 修改绩效状态
     * @param updateAppraisalStatusBO 修改绩效状态对象
     */
    void updateAppraisalStatus(UpdateAppraisalStatusBO updateAppraisalStatusBO);

    /**
     * 查询每个绩效状态的数量
     */
    Map<Integer, Long> queryAppraisalStatusNum();


    /**
     * 查询绩效考核列表
     * @param queryAppraisalPageListBO 查询对象
     * @return 绩效列表
     */
    BasePage<AppraisalPageListVO> queryAppraisalPageList(QueryAppraisalPageListBO queryAppraisalPageListBO);

    /**
     * 查询绩效详情
     * @param appraisalId 绩效id
     * @return 绩效详情
     */
    AppraisalInformationBO queryAppraisalById(Integer appraisalId);

    /**
     * 通过绩效id查询员工列表
     * @param employeeListByAppraisalIdBO 查询对象
     * @return 员工列表
     */
    BasePage<EmployeeListByAppraisalIdVO> queryEmployeeListByAppraisalId(QueryEmployeeListByAppraisalIdBO employeeListByAppraisalIdBO);


    /**
     * 查询员工绩效列表
     * @param employeeListBO 查询对象
     * @return 员工绩效列表
     */
    BasePage<AppraisalEmployeeListVO> queryAppraisalEmployeeList(QueryAppraisalEmployeeListBO employeeListBO);

    /**
     * 员工绩效详情列表
     * @param queryEmployeeAppraisalBO 查询对象
     * @return 员工绩效列表
     */
    BasePage<EmployeeAppraisalVO> queryEmployeeAppraisal(QueryEmployeeAppraisalBO queryEmployeeAppraisalBO);

    /**
     * 查询员工考核详情
     * @param employeeAppraisalId 员工绩效id
     * @return 员工绩效详情
     */
    EmployeeAppraisalDetailVO queryEmployeeDetail(Integer employeeAppraisalId);

    /**
     * 计算分数病保存评定结果记录
     *
     * @param employeeAppraisal 员工考核信息
     */
    void computeScore(HrmAchievementEmployeeAppraisal employeeAppraisal);


    /**
     * 员工绩效详情统计
     * @param employeeId 员工id
     */
    Map<String, Object> queryEmployeeAppraisalCount(Integer employeeId);
}
