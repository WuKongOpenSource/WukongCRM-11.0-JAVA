package com.kakarote.hrm.mapper;

import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import com.kakarote.hrm.entity.BO.EvaluatoListBO;
import com.kakarote.hrm.entity.PO.HrmAchievementEmployeeAppraisal;
import com.kakarote.hrm.entity.VO.EvaluatoListVO;
import com.kakarote.hrm.entity.VO.QueryMyAppraisalVO;
import com.kakarote.hrm.entity.VO.ResultConfirmListVO;
import com.kakarote.hrm.entity.VO.TargetConfirmListVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 员工绩效考核 Mapper 接口
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
public interface HrmAchievementEmployeeAppraisalMapper extends BaseMapper<HrmAchievementEmployeeAppraisal> {

    BasePage<QueryMyAppraisalVO> queryMyAppraisal(BasePage<QueryMyAppraisalVO> parse, @Param("employeeId") Integer employeeId,@Param("status") Integer status);

    BasePage<EvaluatoListVO> evaluatoList(BasePage<Object> parse,
                                          @Param("employeeId") Integer employeeId,
                                          @Param("data") EvaluatoListBO evaluatoListBO);

    BasePage<TargetConfirmListVO> queryTodoAppraisalByStatus(BasePage<TargetConfirmListVO> parse,
                                                             @Param("employeeId") Integer employeeId,
                                                             @Param("status") Integer status,
                                                             @Param("search") String search,
                                                             @Param("appraisalId") Integer appraisalId);
    BasePage<ResultConfirmListVO> queryResultConfirmList(BasePage<Object> parse, @Param("employeeId") Integer employeeId,@Param("search") String search);

    List<Map<String, Object>> queryScoreLevels(Integer appraisalId);

    Map<String, Object> queryAppraisalIdInfo(String appraisalId);

    List<Map<String, Object>> queryEmployeeByLevelId(@Param("levelId") Integer levelId,
                                                     @Param("appraisalId") Integer appraisalId);

    Integer queryWaitingNum(String appraisalId);

    Integer queryTotalNum(String appraisalId);

    List<TargetConfirmListVO> queryTargetConfirmScreen(@Param("employeeId") Integer employeeId,
                                                       @Param("status") Integer status);

    List<EvaluatoListVO> queryEvaluatoScreen(@Param("employeeId")Integer employeeId,@Param("status") Integer status);
}
