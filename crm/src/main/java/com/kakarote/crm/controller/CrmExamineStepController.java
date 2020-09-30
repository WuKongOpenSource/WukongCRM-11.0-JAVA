package com.kakarote.crm.controller;


import com.kakarote.crm.service.ICrmExamineStepService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 审批步骤表 前端控制器
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-28
 */
@RestController
@RequestMapping("/crmExamineStep")
@Api(tags = "审批步骤模块")
public class CrmExamineStepController {

    @Autowired
    private ICrmExamineStepService examineStepService;
}

