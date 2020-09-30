package com.kakarote.work.controller;


import com.kakarote.core.common.R;
import com.kakarote.core.common.Result;
import com.kakarote.work.entity.PO.WorkTaskLabel;
import com.kakarote.work.service.IWorkTaskLabelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 任务标签表 前端控制器
 * </p>
 *
 * @author wyq
 * @since 2020-05-15
 */
@RestController
@RequestMapping("/workTaskLabel")
@Api(tags = "任务标签")
public class WorkTaskLabelController {
    @Autowired
    private IWorkTaskLabelService workTaskLabelService;

    @PostMapping("/saveLabel")
    @ApiOperation("新建任务标签")
    public Result saveLabel(@RequestBody WorkTaskLabel workTaskLabel){
        workTaskLabelService.saveLabel(workTaskLabel);
        return R.ok();
    }

    @PostMapping("/setLabel")
    @ApiOperation("编辑任务标签")
    public Result setLabel(@RequestBody WorkTaskLabel workTaskLabel){
        workTaskLabelService.setLabel(workTaskLabel);
        return R.ok();
    }

    @PostMapping("/deleteLabel")
    @ApiOperation("删除任务标签")
    public Result deleteLabel(@RequestParam("labelId") Integer labelId){
        workTaskLabelService.deleteLabel(labelId);
        return R.ok();
    }

    @PostMapping("/getLabelList")
    @ApiOperation("查看标签列表")
    public Result getLabelList(){
        return R.ok(workTaskLabelService.getLabelList());
    }

    @PostMapping("/updateOrder")
    @ApiOperation("修改标签排序")
    public Result updateOrder(@RequestBody List<Integer> labelIdList){
        workTaskLabelService.updateOrder(labelIdList);
        return R.ok();
    }

    @PostMapping("/queryById/{labelId}")
    @ApiOperation("查看标签详情")
    public Result queryById(@PathVariable Integer labelId){
        return R.ok(workTaskLabelService.queryById(labelId));
    }

    @PostMapping("/getTaskList/{labelId}")
    @ApiOperation("查看标签下任务列表")
    public Result getTaskList(@PathVariable Integer labelId){
        return R.ok(workTaskLabelService.getTaskList(labelId));
    }
}

