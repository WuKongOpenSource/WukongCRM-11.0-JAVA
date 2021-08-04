package com.kakarote.hrm.service;

import com.kakarote.core.servlet.BaseService;
import com.kakarote.hrm.entity.PO.HrmAchievementEmployeeEvaluato;
import com.kakarote.hrm.entity.VO.EvaluatoResultVO;

import java.util.List;

/**
 * <p>
 * 员工绩效结果评定表 服务类
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
public interface IHrmAchievementEmployeeEvaluatoService extends BaseService<HrmAchievementEmployeeEvaluato> {

    /**
     * 查询考核评价列表
     * @param employeeAppraisalId
     * @return
     */
    List<EvaluatoResultVO> queryEvaluatoList(Integer employeeAppraisalId);
}
