package com.kakarote.hrm.controller;


import com.kakarote.core.common.Result;
import com.kakarote.core.common.SubModelType;
import com.kakarote.core.common.log.BehaviorEnum;
import com.kakarote.core.common.log.SysLog;
import com.kakarote.core.common.log.SysLogHandler;
import com.kakarote.hrm.common.log.HrmSalarySlipTemplateLog;
import com.kakarote.hrm.entity.BO.AddSlipTemplateBO;
import com.kakarote.hrm.entity.PO.HrmSalarySlipTemplate;
import com.kakarote.hrm.service.IHrmSalarySlipTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 工资表模板 前端控制器
 * </p>
 *
 * @author hmb
 * @since 2020-11-03
 */
@RestController
@RequestMapping("/hrmSalarySlipTemplate")
@Api(tags = "工资条-工资条模板接口")
@SysLog(subModel = SubModelType.HRM_SALARY_SLIP,logClass = HrmSalarySlipTemplateLog.class)
public class HrmSalarySlipTemplateController {

    @Autowired
    private IHrmSalarySlipTemplateService salarySlipTemplateService;

    @PostMapping("/querySlipTemplateList")
    @ApiOperation("查询工资条模板列表")
    public Result<List<HrmSalarySlipTemplate>> querySlipTemplateList(){
        List<HrmSalarySlipTemplate> slipTemplates = salarySlipTemplateService.list();
        return Result.ok(slipTemplates);
    }

    @PostMapping("/addSlipTemplate")
    @ApiOperation("添加工资条模板")
    @SysLogHandler(behavior = BehaviorEnum.SAVE,object = "#addSlipTemplateBO.templateName",detail = "'添加了工资条模板:'+#addSlipTemplateBO.templateName")
    public Result addSlipTemplate(@Validated @RequestBody AddSlipTemplateBO addSlipTemplateBO){
        salarySlipTemplateService.addSlipTemplate(addSlipTemplateBO);
        return Result.ok();
    }

    @PostMapping("/deleteSlipTemplate/{templateId}")
    @ApiOperation("删除工资条模板")
    @SysLogHandler(behavior = BehaviorEnum.DELETE)
    public Result deleteSlipTemplate(@PathVariable Integer templateId){
        salarySlipTemplateService.deleteSlipTemplate(templateId);
        return Result.ok();
    }

}

