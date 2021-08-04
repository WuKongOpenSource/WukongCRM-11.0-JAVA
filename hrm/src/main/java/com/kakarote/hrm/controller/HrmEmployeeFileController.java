package com.kakarote.hrm.controller;


import com.kakarote.core.common.Result;
import com.kakarote.hrm.entity.BO.AddFileBO;
import com.kakarote.hrm.entity.BO.QueryFileBySubTypeBO;
import com.kakarote.hrm.entity.PO.HrmEmployeeFile;
import com.kakarote.hrm.service.IHrmEmployeeFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 员工附件表 前端控制器
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
@RestController
@RequestMapping("/hrmEmployeeFile")
@Api(tags = "员工管理-员工附件")
public class HrmEmployeeFileController {

    @Autowired
    private IHrmEmployeeFileService employeeFileService;

    @PostMapping("/queryFileNum/{employeeId}")
    @ApiOperation("查询员工总体附件")
    public Result<Map<String,Object>> queryFileNum(@PathVariable Integer employeeId) {
        Map<String,Object> map = employeeFileService.queryFileNum(employeeId);
        return Result.ok(map);
    }

    @PostMapping("/queryFileBySubType")
    @ApiOperation("根据附件类型查询附件详情")
    public Result<List<HrmEmployeeFile>> queryFileBySubType(@RequestBody QueryFileBySubTypeBO queryFileBySubTypeBO) {
        List<HrmEmployeeFile> list = employeeFileService.queryFileBySubType(queryFileBySubTypeBO);
        return Result.ok(list);
    }

    @PostMapping("/addFile")
    @ApiOperation("添加附件")
    public Result addFile(@RequestBody AddFileBO addFileBO) {
        employeeFileService.addFile(addFileBO);
        return Result.ok();
    }


    @PostMapping("/deleteFile/{employeeFileId}")
    @ApiOperation("删除附件")
    public Result deleteFile(@PathVariable Integer employeeFileId) {
        employeeFileService.deleteFile(employeeFileId);
        return Result.ok();
    }
}

