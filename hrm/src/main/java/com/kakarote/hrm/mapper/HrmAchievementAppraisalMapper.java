package com.kakarote.hrm.mapper;

import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import com.kakarote.hrm.entity.BO.QueryAppraisalEmployeeListBO;
import com.kakarote.hrm.entity.BO.QueryAppraisalPageListBO;
import com.kakarote.hrm.entity.BO.QueryEmployeeListByAppraisalIdBO;
import com.kakarote.hrm.entity.PO.HrmAchievementAppraisal;
import com.kakarote.hrm.entity.VO.AppraisalEmployeeListVO;
import com.kakarote.hrm.entity.VO.AppraisalPageListVO;
import com.kakarote.hrm.entity.VO.EmployeeAppraisalVO;
import com.kakarote.hrm.entity.VO.EmployeeListByAppraisalIdVO;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.Map;

/**
 * <p>
 * 绩效考核 Mapper 接口
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
public interface HrmAchievementAppraisalMapper extends BaseMapper<HrmAchievementAppraisal> {

    BasePage<AppraisalPageListVO> queryAppraisalPageList(BasePage<AppraisalPageListVO> parse,@Param("data") QueryAppraisalPageListBO queryAppraisalPageListBO);

    BasePage<EmployeeListByAppraisalIdVO> queryEmployeeListByAppraisalId(BasePage<EmployeeListByAppraisalIdVO> page,
                                                                         @Param("data") QueryEmployeeListByAppraisalIdBO employeeListByAppraisalIdBO);

    BasePage<AppraisalEmployeeListVO> queryEmployeePageList(BasePage<AppraisalEmployeeListVO> parse, @Param("data") QueryAppraisalEmployeeListBO employeeListBO,
                                                            @Param("employeeIds") Collection<Integer> dataAuthEmployeeIds);

    Map<String, Object> queryEmployeeLastAppraisal(Integer employeeId);

    BasePage<EmployeeAppraisalVO> queryEmployeeAppraisal(BasePage<EmployeeAppraisalVO> parse,@Param("employeeId") Integer employeeId);
}
