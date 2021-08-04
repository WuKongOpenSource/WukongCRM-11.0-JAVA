package com.kakarote.oa.controller;

import com.kakarote.core.common.Result;
import com.kakarote.core.utils.ExcelParseUtil;
import com.kakarote.oa.constart.entity.BO.*;
import com.kakarote.oa.constart.entity.PO.WorkTask;
import com.kakarote.oa.constart.entity.VO.OaTaskListVO;
import com.kakarote.oa.service.IOaTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author wyq
 */
@RestController
@RequestMapping("/oaTask")
@Api(tags = "oa任务")
public class OaTaskController {

    @Autowired
    private IOaTaskService oaTaskService;

    @PostMapping("/saveWorkTask")
    public Result saveWorkTask(@RequestBody WorkTask workTask){
        return oaTaskService.saveWorkTask(workTask);
    }

    @PostMapping("/setWorkTaskStatus")
    @ApiOperation("设置项目任务状态")
    public Result setWorkTaskStatus(@RequestBody WorkTaskStatusBO workTaskStatusBO){
        return oaTaskService.setWorkTaskStatus(workTaskStatusBO);
    }

    @PostMapping("/setWorkTaskTitle")
    @ApiOperation("设置项目任务标题")
    public Result setWorkTaskTitle(@RequestBody WorkTaskNameBO workTaskNameBO){
        return oaTaskService.setWorkTaskTitle(workTaskNameBO);
    }

    @PostMapping("/setWorkTaskDescription")
    @ApiOperation("设置项目任务描述")
    public Result setWorkTaskDescription(@RequestBody WorkTaskDescriptionBO workTaskDescriptionBO){
        return oaTaskService.setWorkTaskDescription(workTaskDescriptionBO);

    }

    @PostMapping("/setWorkTaskMainUser")
    @ApiOperation("设置项目任务负责人")
    public Result setWorkTaskMainUser(@RequestBody WorkTaskUserBO workTaskUserBO){
        return oaTaskService.setWorkTaskMainUser(workTaskUserBO);
    }

    @PostMapping("/setWorkTaskOwnerUser")
    @ApiOperation("新建项目任务")
    public Result setWorkTaskOwnerUser(@RequestBody WorkTaskOwnerUserBO workTaskOwnerUserBO){
        return oaTaskService.setWorkTaskOwnerUser(workTaskOwnerUserBO);
    }

    @PostMapping("/setWorkTaskTime")
    @ApiOperation("设置项目任务开始结束时间")
    public Result setWorkTaskTime(@RequestBody WorkTask workTask){
        return oaTaskService.setWorkTaskTime(workTask);
    }

    @PostMapping("/setWorkTaskLabel")
    @ApiOperation("设置项目任务标签")
    public Result setWorkTaskLabel(@RequestBody WorkTaskLabelsBO workTaskLabelsBO){
        return oaTaskService.setWorkTaskLabel(workTaskLabelsBO);
    }

    @PostMapping("/setWorkTaskPriority")
    @ApiOperation("设置项目任务优先级")
    public Result setWorkTaskPriority(@RequestBody WorkTaskPriorityBO workTaskPriorityBO){
        return oaTaskService.setWorkTaskPriority(workTaskPriorityBO);
    }

    @PostMapping("/addWorkChildTask")
    @ApiOperation("新建项目子任务")
    public Result addWorkChildTask(@RequestBody WorkTask workTask){
        return oaTaskService.addWorkChildTask(workTask);
    }

    @PostMapping("/updateWorkChildTask")
    @ApiOperation("编辑项目子任务")
    public Result updateWorkChildTask(@RequestBody WorkTask workTask){
        return oaTaskService.updateWorkChildTask(workTask);
    }

    @PostMapping("/setWorkChildTaskStatus")
    @ApiOperation("设置项目子任务状态")
    public Result setWorkChildTaskStatus(@RequestBody WorkTaskStatusBO workTaskStatusBO){
        return oaTaskService.setWorkChildTaskStatus(workTaskStatusBO);
    }

    @PostMapping("/deleteWorkTaskOwnerUser")
    @ApiOperation("删除项目子任务负责人")
    public Result deleteWorkTaskOwnerUser(@RequestBody WorkTaskUserBO workTaskUserBO){
        return oaTaskService.deleteWorkTaskOwnerUser(workTaskUserBO);
    }

    @PostMapping("/deleteWorkTaskLabel")
    @ApiOperation("删除项目任务标签")
    public Result deleteWorkTaskLabel(@RequestBody WorkTaskLabelBO workTaskLabelBO){
        return oaTaskService.deleteWorkTaskLabel(workTaskLabelBO);
    }

    @PostMapping("/deleteWorkChildTask/{taskId}")
    @ApiOperation("删除子任务")
    public Result deleteWorkChildTask(@PathVariable Integer taskId){
        return oaTaskService.deleteWorkChildTask(taskId);
    }

    @PostMapping("/queryTaskList")
    @ApiOperation("查看oa任务列表")
    public Result<OaTaskListVO> queryTaskList(@RequestBody OaTaskListBO oaTaskListBO){
        Result<OaTaskListVO> listVO = oaTaskService.queryTaskList(oaTaskListBO);
        return listVO;
    }

    @PostMapping("/oaTaskExport")
    @ApiOperation("任务导出")
    public void oaTaskExport(@RequestBody OaTaskListBO oaTaskListBO,HttpServletResponse response) {
        oaTaskListBO.setIsExport(true);
        OaTaskListVO data = oaTaskService.queryTaskList(oaTaskListBO).getData();
        List<Map<String, Object>> list = data.getExportList();
        List<ExcelParseUtil.ExcelDataEntity> dataList = new ArrayList<>();
        dataList.add(ExcelParseUtil.toEntity("name", "任务名称"));
        dataList.add(ExcelParseUtil.toEntity("description", "任务描述"));
        dataList.add(ExcelParseUtil.toEntity("mainUserName", "负责人"));
        dataList.add(ExcelParseUtil.toEntity("startTime", "开始时间"));
        dataList.add(ExcelParseUtil.toEntity("stopTime", "结束时间"));
        dataList.add(ExcelParseUtil.toEntity("labelName", "标签"));
        dataList.add(ExcelParseUtil.toEntity("ownerUserName", "参与人"));
        dataList.add(ExcelParseUtil.toEntity("priority", "优先级"));
        dataList.add(ExcelParseUtil.toEntity("createUserName", "创建人"));
        dataList.add(ExcelParseUtil.toEntity("createTime", "创建时间"));
        dataList.add(ExcelParseUtil.toEntity("relateCrmWork", "关联业务"));
        ExcelParseUtil.exportExcel(list, new ExcelParseUtil.ExcelParseService() {
            @Override
            public void castData(Map<String, Object> record, Map<String, Integer> headMap) {

            }
            @Override
            public String getExcelName() {
                return "任务";
            }
        }, dataList);
    }

}
