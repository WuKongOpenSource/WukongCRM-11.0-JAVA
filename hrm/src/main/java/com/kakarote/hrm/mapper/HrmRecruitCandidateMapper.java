package com.kakarote.hrm.mapper;

import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import com.kakarote.hrm.entity.BO.QueryCandidatePageListBO;
import com.kakarote.hrm.entity.BO.QueryNotesStatusBO;
import com.kakarote.hrm.entity.PO.HrmRecruitCandidate;
import com.kakarote.hrm.entity.VO.CandidatePageListVO;
import org.apache.ibatis.annotations.Param;

import java.util.*;

/**
 * <p>
 * 招聘候选人表 Mapper 接口
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
public interface HrmRecruitCandidateMapper extends BaseMapper<HrmRecruitCandidate> {



    BasePage<CandidatePageListVO> queryPageList(BasePage<CandidatePageListVO> parse, @Param("data") QueryCandidatePageListBO queryCandidatePageListBO,
                                                @Param("deptIds") Collection<Integer> deptIds);

    CandidatePageListVO getById(String candidateId);

    List<Map<String,Object>> queryRecruitListByTime(@Param("time") Date time,@Param("deptIds") Collection<Integer> deptIds);

    List<Map<String,Object>> queryDataAuthCandidateStatus(@Param("deptIds") Collection<Integer> deptIds);

    Set<String> queryRecruitStatusList(@Param("data") QueryNotesStatusBO queryNotesStatusBO,@Param("deptIds") Collection<Integer> deptIds);
}
