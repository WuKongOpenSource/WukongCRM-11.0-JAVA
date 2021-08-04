package com.kakarote.hrm.controller;


import com.kakarote.core.common.Result;
import com.kakarote.hrm.entity.BO.QueryEmployFieldManageBO;
import com.kakarote.hrm.entity.VO.EmployeeFieldManageVO;
import com.kakarote.hrm.service.IHrmEmployeeFieldManageService;
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
 * 自定义字段管理表 前端控制器
 * </p>
 *
 * @author guomenghao
 * @since 2021-04-14
 */
@RestController
@RequestMapping("/hrmEmployeeFieldManage")
@Api(tags = "员工管理-管理员工自定义字段")
public class HrmEmployeeFieldManageController {
    @Autowired
    private IHrmEmployeeFieldManageService employeeFieldManageService;

    @PostMapping("/queryEmployeeManageField")
    @ApiOperation("查询管理可设置员工字段列表")
    public Result<List<EmployeeFieldManageVO>> queryEmployeeManageField(QueryEmployFieldManageBO queryEmployFieldManageBO){

        List<EmployeeFieldManageVO> manageFields = employeeFieldManageService.queryEmployeeManageField(queryEmployFieldManageBO);
        return Result.ok(manageFields);
    }
    @PostMapping("/setEmployeeManageField")
    @ApiOperation("修改管理可以设置员工字段")
    public Result setEmployeeManageField(@RequestBody List<EmployeeFieldManageVO> manageFields){
        employeeFieldManageService.setEmployeeManageField(manageFields);
        return Result.ok();
    }

}

