package com.kakarote.hrm.controller;


import com.kakarote.core.common.Result;
import com.kakarote.hrm.entity.BO.SetTaxRuleBO;
import com.kakarote.hrm.entity.PO.HrmSalaryTaxRule;
import com.kakarote.hrm.service.IHrmSalaryTaxRuleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 计税规则 前端控制器
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-26
 */
@RestController
@RequestMapping("/hrmSalaryTaxRule")
@Api(tags = "薪资管理-计税规则")
public class HrmSalaryTaxRuleController {

    @Autowired
    private IHrmSalaryTaxRuleService salaryTaxRuleService;

    @PostMapping("/queryTaxRuleList")
    @ApiOperation("查询计税规则列表")
    public Result<List<HrmSalaryTaxRule>> queryTaxRuleList(){
        List<HrmSalaryTaxRule> list = salaryTaxRuleService.queryTaxRuleList();
        return Result.ok(list);
    }

    @PostMapping("/setTaxRule")
    @ApiOperation("修改计规则")
    public Result setTaxRule(@RequestBody SetTaxRuleBO setTaxRuleBO){
        salaryTaxRuleService.setTaxRule(setTaxRuleBO);
        return Result.ok();
    }

}

