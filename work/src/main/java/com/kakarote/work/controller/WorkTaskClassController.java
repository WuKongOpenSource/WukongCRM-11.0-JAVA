package com.kakarote.work.controller;


import com.kakarote.core.common.R;
import com.kakarote.core.common.Result;
import com.kakarote.work.entity.PO.WorkTaskClass;
import com.kakarote.work.service.IWorkTaskClassService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 任务分类表 前端控制器
 * </p>
 *
 * @author wyq
 * @since 2020-05-15
 */
@RestController
@RequestMapping("/workTaskClass")
@Api(tags = "任务列表")
public class WorkTaskClassController {
    @Autowired
    private IWorkTaskClassService workTaskClassService;

    @PostMapping("/queryClassNameByWorkId/{workId}")
    @ApiOperation("根据项目id查询分类列表")
    public Result queryClassNameByWorkId(@PathVariable @NotNull Integer workId){
        return R.ok(workTaskClassService.queryClassNameByWorkId(workId));
    }

    @PostMapping("/saveWorkTaskClass")
    @ApiOperation("新建任务列表")
    public Result saveWorkTaskClass(@RequestBody WorkTaskClass workTaskClass){
        workTaskClassService.saveWorkTaskClass(workTaskClass);
        return R.ok();
    }

    @PostMapping("/updateWorkTaskClass")
    @ApiOperation("编辑任务列表")
    public Result updateWorkTaskClass(@RequestBody WorkTaskClass workTaskClass){
        workTaskClassService.updateWorkTaskClass(workTaskClass);
        return R.ok();
    }
}

