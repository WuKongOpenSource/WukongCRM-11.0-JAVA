package com.kakarote.hrm.controller;


import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.hrm.entity.BO.QuerySalaryListBO;
import com.kakarote.hrm.entity.PO.HrmEmployeeSalaryCard;
import com.kakarote.hrm.entity.PO.HrmEmployeeSocialSecurityInfo;
import com.kakarote.hrm.entity.VO.QuerySalaryListVO;
import com.kakarote.hrm.entity.VO.SalaryOptionHeadVO;
import com.kakarote.hrm.entity.VO.SalarySocialSecurityVO;
import com.kakarote.hrm.service.IHrmEmployeeSocialSecurityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 员工工资社保
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
@RestController
@RequestMapping("/hrmEmployee/SocialSecurity")
@Api(tags = "员工管理-员工工资社保")
public class HrmEmployeeSocialSecurityController {

    @Autowired
    private IHrmEmployeeSocialSecurityService socialSecurityService;


    @PostMapping("/salarySocialSecurityInformation/{employeeId}")
    @ApiOperation("工资社保基本信息")
    public Result<SalarySocialSecurityVO> salarySocialSecurityInformation(@PathVariable("employeeId") Integer employeeId){
        SalarySocialSecurityVO salarySocialSecurityVO = socialSecurityService.salarySocialSecurityInformation(employeeId);
        return Result.ok(salarySocialSecurityVO);
    }

    @PostMapping("/addSalaryCard")
    @ApiOperation("添加工资卡")
    public Result addSalaryCard(@RequestBody HrmEmployeeSalaryCard salaryCard){
        socialSecurityService.addOrUpdateSalaryCard(salaryCard);
        return Result.ok();
    }

    @PostMapping("/setSalaryCard")
    @ApiOperation("修改工资卡")
    public Result setSalaryCard(@RequestBody HrmEmployeeSalaryCard salaryCard){
        socialSecurityService.addOrUpdateSalaryCard(salaryCard);
        return Result.ok();
    }

    /**
     * 删除工资卡
     */
    @PostMapping("/deleteSalaryCard/{salaryCardId}")
    @ApiOperation("删除工资卡")
    public Result deleteSalaryCard(@PathVariable("salaryCardId") Integer salaryCardId){
        socialSecurityService.deleteSalaryCard(salaryCardId);
        return Result.ok();
    }

    @PostMapping("/addSocialSecurity")
    @ApiOperation("添加社保信息")
    public Result addSocialSecurity(@RequestBody HrmEmployeeSocialSecurityInfo socialSecurityInfo){
        socialSecurityService.addOrUpdateSocialSecurity(socialSecurityInfo);
        return Result.ok();
    }

    @PostMapping("/setSocialSecurity")
    @ApiOperation("修改社保信息")
    public Result setSocialSecurity(@RequestBody HrmEmployeeSocialSecurityInfo socialSecurityInfo){
        socialSecurityService.addOrUpdateSocialSecurity(socialSecurityInfo);
        return Result.ok();
    }

    @PostMapping("/deleteSocialSecurity/{socialSecurityInfoId}")
    @ApiOperation("删除社保信息")
    public Result deleteSocialSecurity(@PathVariable("socialSecurityInfoId") Integer socialSecurityInfoId){
        socialSecurityService.deleteSocialSecurity(socialSecurityInfoId);
        return Result.ok();
    }

    @PostMapping("/querySalaryList")
    @ApiOperation("查询薪资列表")
    public Result<BasePage<QuerySalaryListVO>> querySalaryList(@RequestBody QuerySalaryListBO querySalaryListBO){
        BasePage<QuerySalaryListVO> page = socialSecurityService.querySalaryList(querySalaryListBO);
        return Result.ok(page);
    }

    @PostMapping("/querySalaryDetail/{sEmpRecordId}")
    @ApiOperation("查询薪资详情")
    public Result<List<SalaryOptionHeadVO>> querySalaryDetail(@PathVariable String sEmpRecordId){
        List<SalaryOptionHeadVO> salaryOptionHeadVOList = socialSecurityService.querySalaryDetail(sEmpRecordId);
        return Result.ok(salaryOptionHeadVOList);
    }


}

