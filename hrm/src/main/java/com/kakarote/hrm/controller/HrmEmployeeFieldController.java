package com.kakarote.hrm.controller;


import com.kakarote.core.common.Result;
import com.kakarote.hrm.entity.BO.UpdateFieldConfigBO;
import com.kakarote.hrm.entity.BO.UpdateFieldWidthBO;
import com.kakarote.hrm.entity.BO.VerifyUniqueBO;
import com.kakarote.hrm.entity.VO.EmployeeHeadFieldVO;
import com.kakarote.hrm.service.IHrmEmployeeFieldService;
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
 * 自定义字段表 前端控制器
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
@RestController
@RequestMapping("/hrmEmployeeField")
@Api(tags = "员工管理-员工自定义字段表")
public class HrmEmployeeFieldController {

    @Autowired
    private IHrmEmployeeFieldService employeeFieldService;


    @PostMapping("/queryListHeads")
    @ApiOperation("查询员工表字段")
    public Result<List<EmployeeHeadFieldVO>> queryListHeads() {
        List<EmployeeHeadFieldVO> employeeListHeadBOField = employeeFieldService.queryListHeads();
        return Result.ok(employeeListHeadBOField);
    }


    @PostMapping("/updateFieldConfig")
    @ApiOperation("批量修改字段表头配置")
    public Result updateFieldConfig(@RequestBody List<UpdateFieldConfigBO> updateFieldConfigBOList){
        employeeFieldService.updateFieldConfig(updateFieldConfigBOList);
        return Result.ok();
    }

    @PostMapping("/verify")
    @ApiOperation("验证唯一")
    public Result verifyUnique(@RequestBody VerifyUniqueBO verifyUniqueBO){
        VerifyUniqueBO verify =  employeeFieldService.verifyUnique(verifyUniqueBO);
        return Result.ok(verify);
    }

    @PostMapping("/updateFieldWidth")
    @ApiOperation("修改字段宽度")
    public Result updateFieldWidth(@RequestBody UpdateFieldWidthBO updateFieldWidthBO){
        employeeFieldService.updateFieldWidth(updateFieldWidthBO);
        return Result.ok();
    }

}

