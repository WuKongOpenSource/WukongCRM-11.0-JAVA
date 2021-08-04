package com.kakarote.hrm.mapper;

import com.kakarote.core.servlet.BaseMapper;
import com.kakarote.hrm.entity.PO.HrmAchievementEmployeeEvaluato;
import com.kakarote.hrm.entity.VO.EvaluatoResultVO;

import java.util.List;

/**
 * <p>
 * 员工绩效结果评定表 Mapper 接口
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
public interface HrmAchievementEmployeeEvaluatoMapper extends BaseMapper<HrmAchievementEmployeeEvaluato> {

    List<EvaluatoResultVO> queryEvaluatoList(Integer employeeAppraisalId);
}
