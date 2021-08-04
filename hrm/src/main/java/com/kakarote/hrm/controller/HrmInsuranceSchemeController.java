package com.kakarote.hrm.controller;


import com.kakarote.core.common.Result;
import com.kakarote.hrm.entity.BO.QueryInsuranceScaleBO;
import com.kakarote.hrm.entity.BO.QueryInsuranceTypeBO;
import com.kakarote.hrm.entity.PO.HrmInsuranceScheme;
import com.kakarote.hrm.service.IHrmInsuranceSchemeService;
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
 * 社保方案表 前端控制器
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
@RestController
@RequestMapping("/hrmInsuranceScheme")
@Api(tags = "员工管理-社保方案接口")
public class HrmInsuranceSchemeController {

    @Autowired
    private IHrmInsuranceSchemeService insuranceSchemeService;

    @PostMapping("/queryInsuranceSchemeList")
    @ApiOperation("查询社保方案列表")
    public Result<List<HrmInsuranceScheme>> queryInsuranceSchemeList(){
        List<HrmInsuranceScheme> insuranceSchemeList = insuranceSchemeService.queryInsuranceSchemeList();
        return Result.ok(insuranceSchemeList);
    }

    @PostMapping("/queryInsuranceType")
    @ApiOperation("查询社保类型(调用社保100)")
    public Result<String> queryInsuranceType(@RequestBody QueryInsuranceTypeBO queryInsuranceTypeBO){
       String str =  insuranceSchemeService.queryInsuranceType(queryInsuranceTypeBO);
        return Result.ok(str);
    }

    @PostMapping("/queryInsuranceScale")
    @ApiOperation("查询社保比例(调用社保100)")
    public Result<String> queryInsuranceScale(@RequestBody QueryInsuranceScaleBO queryInsuranceScaleBO){
        String str =  insuranceSchemeService.queryInsuranceScale(queryInsuranceScaleBO);
        return Result.ok(str);
    }
}

