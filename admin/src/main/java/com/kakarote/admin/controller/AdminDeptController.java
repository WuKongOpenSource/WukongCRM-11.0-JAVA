package com.kakarote.admin.controller;


import com.kakarote.admin.common.log.AdminDeptLog;
import com.kakarote.admin.entity.BO.AdminDeptBO;
import com.kakarote.admin.entity.BO.AdminDeptQueryBO;
import com.kakarote.admin.entity.VO.AdminDeptVO;
import com.kakarote.admin.service.IAdminDeptService;
import com.kakarote.core.common.ApiExplain;
import com.kakarote.core.common.R;
import com.kakarote.core.common.Result;
import com.kakarote.core.common.SubModelType;
import com.kakarote.core.common.log.BehaviorEnum;
import com.kakarote.core.common.log.SysLog;
import com.kakarote.core.common.log.SysLogHandler;
import com.kakarote.core.feign.admin.entity.SimpleDept;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 部门表 前端控制器
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-04-27
 */
@RestController
@RequestMapping("/adminDept")
@Api(tags = "部门管理相关接口")
@SysLog(subModel = SubModelType.ADMIN_DEPARTMENT_MANAGEMENT,logClass = AdminDeptLog.class)
public class AdminDeptController {

    @Autowired
    private IAdminDeptService adminDeptService;

    @PostMapping("/queryDeptTree")
    @ApiOperation("查询部门列表树")
    public Result<List<AdminDeptVO>> queryDeptTree(@RequestBody AdminDeptQueryBO queryBO) {
        List<AdminDeptVO> deptList = adminDeptService.queryDeptTree(queryBO);
        return Result.ok(deptList);
    }

    @PostMapping("/addDept")
    @ApiOperation("新增部门")
    @SysLogHandler(behavior = BehaviorEnum.SAVE,object = "#adminDept.name",detail = "'添加了部门:'+#adminDept.name")
    public Result addDept(@RequestBody @Valid AdminDeptBO adminDept) {
        adminDeptService.addDept(adminDept);
        return Result.ok();
    }

    @PostMapping("/setDept")
    @ApiOperation("修改部门")
    @SysLogHandler(behavior = BehaviorEnum.SAVE,object = "#adminDept.name",detail = "'修改了部门:'+#adminDept.name")
    public Result setDept(@RequestBody @Valid AdminDeptBO adminDept) {
        adminDeptService.setDept(adminDept);
        return Result.ok();
    }

    @PostMapping("/deleteDept/{deptId}")
    @ApiOperation("删除部门")
    @SysLogHandler(behavior = BehaviorEnum.DELETE)
    public Result deleteDept(@PathVariable("deptId") Integer deptId) {
        adminDeptService.deleteDept(deptId);
        return Result.ok();
    }

    @RequestMapping("/getNameByDeptId")
    @ApiExplain("根据部门ID获取部门名称")
    public Result getNameByDeptId(Integer deptId) {
        return R.ok(adminDeptService.getNameByDeptId(deptId));
    }

    @RequestMapping("/queryChildDeptId")
    @ApiExplain("根据部门ID下的子部门")
    public Result<List<Integer>> queryChildDeptId(@NotNull Integer deptId) {
        return R.ok(adminDeptService.queryChildDept(deptId));
    }

    @PostMapping("/queryDeptByIds")
    @ApiExplain("根据部门ID获取用户")
    public Result<List<SimpleDept>> queryDeptByIds(@RequestBody List<Integer> ids) {
        List<SimpleDept> simpleDepts = adminDeptService.queryDeptByIds(ids);
        return R.ok(simpleDepts);
    }
}

