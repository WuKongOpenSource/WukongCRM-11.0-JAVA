package com.kakarote.crm.service.impl;

import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.crm.entity.PO.CrmExamineStep;
import com.kakarote.crm.mapper.CrmExamineStepMapper;
import com.kakarote.crm.service.ICrmExamineLogService;
import com.kakarote.crm.service.ICrmExamineService;
import com.kakarote.crm.service.ICrmExamineStepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 审批步骤表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-28
 */
@Service
public class CrmExamineStepServiceImpl extends BaseServiceImpl<CrmExamineStepMapper, CrmExamineStep> implements ICrmExamineStepService {

    @Autowired
    private ICrmExamineService examineService;

    @Autowired
    private ICrmExamineLogService examineLogService;

    @Autowired
    private AdminService adminService;

}
