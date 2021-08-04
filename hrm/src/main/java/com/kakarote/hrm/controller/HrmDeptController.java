package com.kakarote.hrm.controller;


import com.kakarote.core.common.Result;
import com.kakarote.core.common.SubModelType;
import com.kakarote.core.common.log.BehaviorEnum;
import com.kakarote.core.common.log.SysLog;
import com.kakarote.core.common.log.SysLogHandler;
import com.kakarote.core.entity.BasePage;
import com.kakarote.hrm.common.log.HrmDeptLog;
import com.kakarote.hrm.entity.BO.AddDeptBO;
import com.kakarote.hrm.entity.BO.QueryDeptListBO;
import com.kakarote.hrm.entity.BO.QueryEmployeeByDeptIdBO;
import com.kakarote.hrm.entity.VO.DeptVO;
import com.kakarote.hrm.entity.VO.QueryEmployeeListByDeptIdVO;
import com.kakarote.hrm.service.IHrmDeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 部门表 前端控制器
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
@RestController
@RequestMapping("/hrmDept")
@Api(tags = "组织管理-部门")
@SysLog(subModel = SubModelType.HRM_DEPT,logClass = HrmDeptLog.class)
public class HrmDeptController {

    @Autowired
    private IHrmDeptService deptService;

    @PostMapping("/addDept")
    @ApiOperation("添加部门")
    @SysLogHandler(behavior = BehaviorEnum.SAVE,object = "#addDeptBO.name",detail = "'添加了部门:'+#addDeptBO.name")
    public Result addDept(@Valid @RequestBody AddDeptBO addDeptBO){
        deptService.addOrUpdate(addDeptBO);
        return Result.ok();
    }

    @PostMapping("/setDept")
    @ApiOperation("修改部门")
    @SysLogHandler(behavior = BehaviorEnum.UPDATE)
    public Result setDept(@Valid @RequestBody AddDeptBO addDeptBO){
        deptService.addOrUpdate(addDeptBO);
        return Result.ok();
    }

    @PostMapping("/queryById/{deptId}")
    @ApiOperation("查询部门详情")
    public Result<DeptVO> queryById(@PathVariable("deptId") Integer deptId){
        DeptVO deptVO = deptService.queryById(deptId);
        return Result.ok(deptVO);
    }

    @PostMapping("/queryTreeList")
    @ApiOperation("查询部门列表")
    public Result<List<DeptVO>> queryTreeList(@RequestBody QueryDeptListBO queryDeptListBO){
        List<DeptVO> treeNode = deptService.queryTreeList(queryDeptListBO);
        return Result.ok(treeNode);
    }

    @PostMapping("/queryEmployeeByDeptId")
    @ApiOperation("通过部门id查询员工列表")
    public Result<BasePage<QueryEmployeeListByDeptIdVO>> queryEmployeeByDeptId(@RequestBody QueryEmployeeByDeptIdBO employeeByDeptIdBO){
        BasePage<QueryEmployeeListByDeptIdVO> page = deptService.queryEmployeeByDeptId(employeeByDeptIdBO);
        return Result.ok(page);
    }

    @PostMapping("/deleteDeptById/{deptId}")
    @ApiOperation("删除部门")
    @SysLogHandler(behavior = BehaviorEnum.DELETE)
    public Result deleteDeptById(@PathVariable String deptId){
        deptService.deleteDeptById(deptId);
        return Result.ok();
    }


}

