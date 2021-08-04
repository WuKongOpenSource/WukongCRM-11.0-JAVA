package com.kakarote.hrm.service.impl;

import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.hrm.entity.PO.HrmAchievementEmployeeEvaluato;
import com.kakarote.hrm.entity.VO.EvaluatoResultVO;
import com.kakarote.hrm.mapper.HrmAchievementEmployeeEvaluatoMapper;
import com.kakarote.hrm.service.IHrmAchievementEmployeeEvaluatoService;
import com.kakarote.hrm.service.IHrmEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 员工绩效结果评定表 服务实现类
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
@Service
public class HrmAchievementEmployeeEvaluatoServiceImpl extends BaseServiceImpl<HrmAchievementEmployeeEvaluatoMapper, HrmAchievementEmployeeEvaluato> implements IHrmAchievementEmployeeEvaluatoService {


    @Autowired
    private IHrmEmployeeService iHrmEmployeeService;

    @Autowired
    private HrmAchievementEmployeeEvaluatoMapper evaluatoMapper;

    @Override
    public List<EvaluatoResultVO> queryEvaluatoList(Integer employeeAppraisalId) {
        return evaluatoMapper.queryEvaluatoList(employeeAppraisalId);
    }
}
