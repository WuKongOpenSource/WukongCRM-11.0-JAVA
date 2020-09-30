package com.kakarote.oa.service.impl;

import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.oa.entity.PO.OaExamineStep;
import com.kakarote.oa.mapper.OaExamineStepMapper;
import com.kakarote.oa.service.IOaExamineStepService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 审批步骤表 服务实现类
 * </p>
 *
 * @author wyq
 * @since 2020-05-15
 */
@Service
public class OaExamineStepServiceImpl extends BaseServiceImpl<OaExamineStepMapper, OaExamineStep> implements IOaExamineStepService {



    @Override
    public OaExamineStep queryExamineStepByNextExamineIdOrderByStepId(Integer categoryId, Integer examineStepId) {
        return getBaseMapper().queryExamineStepByNextExamineIdOrderByStepId(categoryId,examineStepId);
    }
}
