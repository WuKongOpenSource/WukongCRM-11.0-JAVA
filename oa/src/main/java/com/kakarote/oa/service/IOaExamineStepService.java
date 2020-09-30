package com.kakarote.oa.service;

import com.kakarote.core.servlet.BaseService;
import com.kakarote.oa.entity.PO.OaExamineStep;

/**
 * <p>
 * 审批步骤表 服务类
 * </p>
 *
 * @author wyq
 * @since 2020-05-15
 */
public interface IOaExamineStepService extends BaseService<OaExamineStep> {


    /**
     固定审批
    查询下一个审批步骤
     */
    OaExamineStep queryExamineStepByNextExamineIdOrderByStepId(Integer categoryId, Integer examineStepId);
}
