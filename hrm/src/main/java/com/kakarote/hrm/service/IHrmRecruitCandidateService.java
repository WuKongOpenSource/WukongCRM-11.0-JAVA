package com.kakarote.hrm.service;

import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;
import com.kakarote.core.servlet.upload.FileEntity;
import com.kakarote.hrm.entity.BO.*;
import com.kakarote.hrm.entity.PO.HrmRecruitCandidate;
import com.kakarote.hrm.entity.VO.CandidatePageListVO;

import java.util.*;

/**
 * <p>
 * 招聘候选人表 服务类
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
public interface IHrmRecruitCandidateService extends BaseService<HrmRecruitCandidate> {

    /**
     * 查询候选人列表页
     * @param queryCandidatePageListBO
     * @return
     */
    BasePage<CandidatePageListVO> queryCandidateList(QueryCandidatePageListBO queryCandidatePageListBO);

    /**
     * 查询基本信息
     * @param candidateId
     * @return
     */
    CandidatePageListVO queryById(String candidateId);

    /**
     * 新建候选人
     * @param hrmRecruitCandidate
     */
    void addCandidate(HrmRecruitCandidate hrmRecruitCandidate);

    /**
     * 编辑候选人
     * @param hrmRecruitCandidate
     */
    void setCandidate(HrmRecruitCandidate hrmRecruitCandidate);

    /**
     * 删除候选人
     * @param candidateId
     */
    void deleteById(Integer candidateId);

    /**
     * 批量修改候选人状态
     * @param updateCandidateStatusBO
     */
    void updateCandidateStatus(UpdateCandidateStatusBO updateCandidateStatusBO);

    /**
     * 批量修改候选人职位
     * @param updateCandidatePostBO
     */
    void updateCandidatePost(UpdateCandidatePostBO updateCandidatePostBO);

    /**
     * 批量修改候选人招聘渠道
     * @param updateCandidateRecruitChannelBO
     */
    void updateCandidateRecruitChannel(UpdateCandidateRecruitChannelBO updateCandidateRecruitChannelBO);

    /**
     * 查询一键清理候选人,查询完之后调用修改状态接口
     * @param queryCleanCandidateBO
     * @return
     */
    List<Integer> queryCleanCandidateIds(QueryCleanCandidateBO queryCleanCandidateBO);

    /**
     * 查询候选人附件
     * @param candidateId
     * @return
     */
    Result<List<FileEntity>> queryFile(Integer candidateId);

    /**
     * 查询每个候选人状态的数量
     * @return
     */
    Map<Integer, Long> queryCandidateStatusNum();


    /**
     * 淘汰/流失候选人
     * @param eliminateCandidateBO
     */
    void eliminateCandidate(EliminateCandidateBO eliminateCandidateBO);

    /**
     * 删除候选人
     * @param candidateIds
     */
    void deleteByIds(List<Integer> candidateIds);

    /**
     * 查询候选人列表
     * @return
     */
    List<Map<String,Object>> queryRecruitListByTime(Date time, Collection<Integer> deptIds);

    /**
     * 根据候选人面试时间查询面试状态
     */
    Set<String> queryRecruitStatusList(QueryNotesStatusBO queryNotesStatusBO, Collection<Integer> deptIds);
}
