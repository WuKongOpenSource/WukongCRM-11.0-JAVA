package com.kakarote.hrm.service;

import com.kakarote.hrm.entity.BO.QueryNotesByTimeBO;
import com.kakarote.hrm.entity.BO.QueryNotesStatusBO;
import com.kakarote.hrm.entity.VO.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IHrmDashboardService {

    /**
     * 人事概况
     * @return
     */
    Map<Integer, Long> employeeSurvey();

    /**
     * 招聘动态
     * @return
     */
    RecruitSurveyCountVO recruitSurvey();

    /**
     * 上月薪资概况
     * @return
     */
    LastSalarySurveyVO lastSalarySurvey();

    /**
     * 绩效概况
     * @param status
     * @return
     */
    List<AppraisalSurveyVO> appraisalSurvey(String status);

    /**
     * 绩效概况统计
     * @return
     */
    Map<Integer, Integer> appraisalCountSurvey();


    /**
     * 待办提醒
     * @return
     */
    DoRemindVO toDoRemind();

    /**
     * 查询当日事项
     * @param queryNotesByTimeBO
     * @return
     */
    List<NotesVO> queryNotesByTime(QueryNotesByTimeBO queryNotesByTimeBO);

    /**
     * 查询当日事项
     * @param queryNotesByTimeBO
     * @return
     */
    Set<String> queryNotesStatus(QueryNotesStatusBO queryNotesByTimeBO);

    /**
     * 我的团队(上级角色)
     * @return
     */
    Map<Integer, Long> myTeam();

    /**
     * 团队概况(上级角色)
     * @return
     */
    TeamSurveyVO teamSurvey();

    /**
     * 我的概况(员工端)
     * @return
     */
    MySurveyVO mySurvey();


    /**
     * 查询当日事项(员工端)
     * @param queryNotesByTimeBO
     * @return
     */
    Set<String> myNotesStatus(QueryNotesStatusBO queryNotesByTimeBO);

    /**
     * 查询当日事项(员工端)
     * @param queryNotesByTimeBO
     * @return
     */
    List<NotesVO> myNotesByTime(QueryNotesByTimeBO queryNotesByTimeBO);
}
