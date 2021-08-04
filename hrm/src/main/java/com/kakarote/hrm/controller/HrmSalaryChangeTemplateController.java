package com.kakarote.hrm.controller;


import com.kakarote.core.common.Result;
import com.kakarote.core.common.SubModelType;
import com.kakarote.core.common.log.BehaviorEnum;
import com.kakarote.core.common.log.SysLog;
import com.kakarote.core.common.log.SysLogHandler;
import com.kakarote.hrm.common.log.HrmSalaryChangeTemplateLog;
import com.kakarote.hrm.entity.BO.SetChangeTemplateBO;
import com.kakarote.hrm.entity.VO.ChangeSalaryOptionVO;
import com.kakarote.hrm.entity.VO.QueryChangeTemplateListVO;
import com.kakarote.hrm.service.IHrmSalaryChangeTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 调薪模板 前端控制器
 * </p>
 *
 * @author hmb
 * @since 2020-11-05
 */
@RestController
@RequestMapping("/hrmSalaryChangeTemplate")
@Api(tags = "薪资档案-调薪模板")
@SysLog(subModel = SubModelType.HRM_SALARY_FILE,logClass = HrmSalaryChangeTemplateLog.class)
public class HrmSalaryChangeTemplateController {

    @Autowired
    private IHrmSalaryChangeTemplateService salaryChangeTemplateService;

    @PostMapping("/queryChangeSalaryOption")
    @ApiOperation("查询调薪默认项")
    public Result<List<ChangeSalaryOptionVO>> queryChangeSalaryOption(){
        List<ChangeSalaryOptionVO> list = salaryChangeTemplateService.queryChangeSalaryOption();
        return Result.ok(list);
    }

    @PostMapping("/setChangeTemplate")
    @ApiOperation("设置定薪/调薪模板")
    @SysLogHandler(behavior = BehaviorEnum.SAVE,object = "#setChangeTemplateBO.templateName",detail = "'添加定薪调薪模板'+#setChangeTemplateBO.templateName")
    public Result setChangeTemplate(@RequestBody SetChangeTemplateBO setChangeTemplateBO){
        salaryChangeTemplateService.setChangeTemplate(setChangeTemplateBO);
        return Result.ok();
    }

    @PostMapping("/queryChangeTemplateList")
    @ApiOperation("查询模板列表")
    public Result<List<QueryChangeTemplateListVO>> queryChangeTemplateList(){
        List<QueryChangeTemplateListVO> list = salaryChangeTemplateService.queryChangeTemplateList();
        return Result.ok(list);
    }

    @PostMapping("/deleteChangeTemplate/{id}")
    @ApiOperation("删除模板")
    public Result deleteChangeTemplate(@PathVariable Integer id){
        salaryChangeTemplateService.deleteChangeTemplate(id);
        return Result.ok();
    }
}

