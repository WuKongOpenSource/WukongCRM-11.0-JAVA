package com.kakarote.hrm.controller;


import com.kakarote.core.common.Result;
import com.kakarote.core.common.SubModelType;
import com.kakarote.core.common.log.BehaviorEnum;
import com.kakarote.core.common.log.SysLog;
import com.kakarote.core.common.log.SysLogHandler;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.PageEntity;
import com.kakarote.hrm.common.log.HrmSalaryGroupLog;
import com.kakarote.hrm.entity.BO.SetSalaryGroupBO;
import com.kakarote.hrm.entity.VO.SalaryGroupPageListVO;
import com.kakarote.hrm.service.IHrmSalaryGroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 薪资组 前端控制器
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-26
 */
@RestController
@RequestMapping("/hrmSalaryGroup")
@Api(tags = "薪资管理-薪资组")
@SysLog(logClass = HrmSalaryGroupLog.class)
public class HrmSalaryGroupController {

    @Autowired
    private IHrmSalaryGroupService salaryGroupService;



    @PostMapping("/addSalaryGroup")
    @ApiOperation("添加薪资组")
    @SysLogHandler(applicationName = "admin",subModel = SubModelType.ADMIN_HUMAN_RESOURCE_MANAGEMENT,behavior = BehaviorEnum.SAVE,object = "#salaryGroup.groupName",detail = "'新建了薪资组:'+#salaryGroup.groupName")
    public Result addSalaryGroup(@RequestBody SetSalaryGroupBO salaryGroup){
        salaryGroupService.setSalaryGroup(salaryGroup);
        return Result.ok();
    }


    @PostMapping("/setSalaryGroup")
    @ApiOperation("修改薪资组")
    @SysLogHandler(applicationName = "admin",subModel = SubModelType.ADMIN_HUMAN_RESOURCE_MANAGEMENT,behavior = BehaviorEnum.SAVE,object = "#salaryGroup.groupName",detail = "'修改了薪资组:'+#salaryGroup.groupName")
    public Result setSalaryGroup(@RequestBody SetSalaryGroupBO salaryGroup){
        salaryGroupService.setSalaryGroup(salaryGroup);
        return Result.ok();
    }


    @PostMapping("/querySalaryGroupPageList")
    @ApiOperation("查询薪资组列表")
    public Result<BasePage<SalaryGroupPageListVO>> querySalaryGroupPageList(@RequestBody PageEntity pageEntity){
        BasePage<SalaryGroupPageListVO> page = salaryGroupService.querySalaryGroupPageList(pageEntity);
        return Result.ok(page);
    }


    @PostMapping("/delete/{groupId}")
    @ApiOperation("删除薪资组")
    @SysLogHandler(applicationName = "admin",subModel = SubModelType.ADMIN_HUMAN_RESOURCE_MANAGEMENT,behavior = BehaviorEnum.DELETE)
    public Result deleteSalaryGroup(@PathVariable Integer groupId){
        salaryGroupService.removeById(groupId);
        return Result.ok();
    }


}

