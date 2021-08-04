package com.kakarote.hrm.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kakarote.core.common.Result;
import com.kakarote.hrm.entity.PO.HrmSalaryConfig;
import com.kakarote.hrm.entity.VO.QueryInItConfigVO;
import com.kakarote.hrm.service.IHrmSalaryConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 薪资初始配置 前端控制器
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-26
 */
@RestController
@RequestMapping("/hrmSalaryConfig")
@Api(tags = "薪资管理-薪资初始化配置")
public class HrmSalaryConfigController {

    @Autowired
    private IHrmSalaryConfigService salaryConfigService;

    @PostMapping("/queryInItConfig")
    @ApiOperation("查询薪资/社保初始化配置是否存在")
    public Result<QueryInItConfigVO> queryInItConfig(){
        QueryInItConfigVO queryInItConfigVO = salaryConfigService.queryInItConfig();
        return Result.ok(queryInItConfigVO);
    }


    @PostMapping("/updateInitStatus/{type}")
    @ApiOperation("修改初始化配置状态为已配置")
    public Result updateInitStatus(@PathVariable Integer type){
        salaryConfigService.updateInitStatus(type);
        return Result.ok();
    }

    @PostMapping("/saveInitConfig")
    @ApiOperation("保存初始化配置")
    public Result saveInitConfig(@RequestBody HrmSalaryConfig salaryConfig){
        salaryConfigService.saveInitConfig(salaryConfig);
        return Result.ok();
    }


    @PostMapping("/querySalaryConfig")
    @ApiOperation("查询薪资设置")
    public Result<HrmSalaryConfig> querySalaryConfig(){
        HrmSalaryConfig salaryConfig = salaryConfigService.getOne(Wrappers.emptyWrapper());
        return Result.ok(salaryConfig);
    }

    @PostMapping("/updateSalaryConfig")
    @ApiOperation("修改薪资对应社保月设置")
    public Result updateSalaryConfig(@RequestBody HrmSalaryConfig salaryConfig){
        HrmSalaryConfig salaryConfig1 = salaryConfigService.getOne(Wrappers.emptyWrapper());
        if (salaryConfig1!=null){
            salaryConfigService.lambdaUpdate()
                    .set(HrmSalaryConfig::getSocialSecurityMonthType,salaryConfig.getSocialSecurityMonthType())
                    .eq(HrmSalaryConfig::getConfigId,salaryConfig1.getConfigId()).update();
        }
        return Result.ok();
    }

}

