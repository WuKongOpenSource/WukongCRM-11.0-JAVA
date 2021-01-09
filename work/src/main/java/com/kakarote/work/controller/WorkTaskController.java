package com.kakarote.work.controller;


import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.kakarote.core.common.R;
import com.kakarote.core.common.Result;
import com.kakarote.core.common.SubModelType;
import com.kakarote.core.common.log.BehaviorEnum;
import com.kakarote.core.common.log.SysLog;
import com.kakarote.core.common.log.SysLogHandler;
import com.kakarote.work.common.log.WorkTaskLog;
import com.kakarote.work.entity.BO.*;
import com.kakarote.work.entity.PO.WorkTask;
import com.kakarote.work.service.IWorkTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 任务表 前端控制器
 * </p>
 *
 * @author wyq
 * @since 2020-05-18
 */
@RestController
@RequestMapping("/workTask")
@Api(tags = "任务")
@Slf4j
@SysLog(subModel = SubModelType.WORK_TASK,logClass = WorkTaskLog.class)
public class WorkTaskController {
    @Autowired
    private IWorkTaskService workTaskService;

    @PostMapping("/myTask")
    @ApiOperation("工作台我的任务")
    public Result myTask(@RequestBody WorkTaskNameBO workTaskNameBO){
        return R.ok(workTaskService.myTask(workTaskNameBO,false));
    }

    @PostMapping("/updateTop")
    @ApiOperation("工作台移动任务")
    public Result updateTop(@RequestBody UpdateTaskTopBo updateTaskClassBo){
        workTaskService.updateTop(updateTaskClassBo);
        return R.ok();
    }

    @PostMapping("/saveWorkTask")
    @ApiOperation("新建项目任务")
    @SysLogHandler(behavior = BehaviorEnum.SAVE,object = "#workTask.name",detail = "'新建了任务:'+#workTask.name")
    public Result saveWorkTask(@RequestBody WorkTask workTask){
        workTaskService.saveWorkTask(workTask);
        return R.ok();
    }


    @PostMapping("/updateWorkTask")
    @ApiOperation("编辑项目任务")
    public Result<Boolean> updateWorkTask(@RequestBody WorkTask workTask){
        return R.ok( workTaskService.updateWorkTask(workTask));
    }

    @PostMapping("/setWorkTaskStatus")
    @ApiOperation("设置项目任务状态")
    @SysLogHandler(behavior = BehaviorEnum.UPDATE)
    public Result setWorkTaskStatus(@RequestBody WorkTaskStatusBO workTaskStatusBO){
        workTaskService.setWorkTaskStatus(workTaskStatusBO);
        return R.ok();
    }

    @PostMapping("/setWorkTaskTitle")
    @ApiOperation("设置项目任务标题")
    @SysLogHandler(behavior = BehaviorEnum.UPDATE)
    public Result setWorkTaskTitle(@RequestBody WorkTaskNameBO workTaskNameBO){
        workTaskService.setWorkTaskTitle(workTaskNameBO);
        return R.ok();
    }

    @PostMapping("/setWorkTaskDescription")
    @ApiOperation("设置项目任务描述")
    @SysLogHandler(behavior = BehaviorEnum.UPDATE)
    public Result setWorkTaskDescription(@RequestBody WorkTaskDescriptionBO workTaskDescriptionBO){
        workTaskService.setWorkTaskDescription(workTaskDescriptionBO);
        return R.ok();
    }

    @PostMapping("/setWorkTaskMainUser")
    @ApiOperation("设置项目任务负责人")
    @SysLogHandler(behavior = BehaviorEnum.UPDATE)
    public Result setWorkTaskMainUser(@RequestBody WorkTaskUserBO workTaskUserBO){
        workTaskService.setWorkTaskMainUser(workTaskUserBO);
        return R.ok();
    }

    @PostMapping("/setWorkTaskOwnerUser")
    @ApiOperation("设置项目任务参与人")
    @SysLogHandler(behavior = BehaviorEnum.UPDATE)
    public Result setWorkTaskOwnerUser(@RequestBody WorkTaskOwnerUserBO workTaskOwnerUserBO){
        workTaskService.setWorkTaskOwnerUser(workTaskOwnerUserBO);
        return R.ok();
    }

    @PostMapping("/setWorkTaskTime")
    @ApiOperation("设置项目任务开始结束时间")
    @SysLogHandler(behavior = BehaviorEnum.UPDATE)
    public Result setWorkTaskTime(@RequestBody WorkTask workTask){
        workTaskService.setWorkTaskTime(workTask);
        return R.ok();
    }

    @PostMapping("/setWorkTaskLabel")
    @ApiOperation("设置项目任务标签")
    @SysLogHandler(behavior = BehaviorEnum.UPDATE)
    public Result setWorkTaskLabel(@RequestBody WorkTaskLabelsBO workTaskLabelsBO){
        workTaskService.setWorkTaskLabel(workTaskLabelsBO);
        return R.ok();
    }

    @PostMapping("/setWorkTaskPriority")
    @ApiOperation("设置项目任务优先级")
    @SysLogHandler(behavior = BehaviorEnum.UPDATE)
    public Result setWorkTaskPriority(@RequestBody WorkTaskPriorityBO workTaskPriorityBO){
        workTaskService.setWorkTaskPriority(workTaskPriorityBO);
        return R.ok();
    }

    @PostMapping("/addWorkChildTask")
    @ApiOperation("新建项目子任务")
    @SysLogHandler(behavior = BehaviorEnum.SAVE)
    public Result<WorkTask> addWorkChildTask(@RequestBody WorkTask workTask){
        WorkTask task = workTaskService.addWorkChildTask(workTask);
        return R.ok(task);
    }

    @PostMapping("/updateWorkChildTask")
    @ApiOperation("编辑项目子任务")
    public Result updateWorkChildTask(@RequestBody WorkTask workTask){
        workTaskService.updateWorkChildTask(workTask);
        return R.ok();
    }

    @PostMapping("/setWorkChildTaskStatus")
    @ApiOperation("设置项目子任务状态")
    public Result setWorkChildTaskStatus(@RequestBody WorkTaskStatusBO workTaskStatusBO){
        workTaskService.setWorkChildTaskStatus(workTaskStatusBO);
        return R.ok();
    }

    @PostMapping("/deleteWorkTaskOwnerUser")
    @ApiOperation("删除项目子任务负责人")
    public Result deleteWorkTaskOwnerUser(@RequestBody WorkTaskUserBO workTaskUserBO){
        workTaskService.deleteWorkTaskOwnerUser(workTaskUserBO);
        return R.ok();
    }

    @PostMapping("/deleteWorkTaskLabel")
    @ApiOperation("删除项目任务标签")
    @SysLogHandler(behavior = BehaviorEnum.UPDATE)
    public Result deleteWorkTaskLabel(@RequestBody WorkTaskLabelBO workTaskLabelBO){
        workTaskService.deleteWorkTaskLabel(workTaskLabelBO);
        return R.ok();
    }

    @PostMapping("/queryTaskInfo/{taskId}")
    @ApiOperation("查询任务详情")
    public Result queryTaskInfo(@PathVariable Integer taskId){
        return R.ok(workTaskService.queryTaskInfo(taskId));
    }

    @PostMapping("/queryTrashTaskInfo")
    @ApiOperation("查询回收站任务详情")
    public Result queryTrashTaskInfo(@RequestParam("taskId") Integer taskId){
        return R.ok(workTaskService.queryTrashTaskInfo(taskId));
    }

    @PostMapping("/deleteWorkTask/{taskId}")
    @ApiOperation("删除任务")
    @SysLogHandler(behavior = BehaviorEnum.DELETE)
    public Result deleteWorkTask(@PathVariable Integer taskId){
        workTaskService.deleteWorkTask(taskId);
        return R.ok();
    }

    @PostMapping("/deleteWorkChildTask/{taskId}")
    @ApiOperation("删除子任务")
    @SysLogHandler(behavior = BehaviorEnum.DELETE)
    public Result deleteWorkChildTask(@PathVariable Integer taskId){
        workTaskService.deleteWorkTask(taskId);
        return R.ok();
    }

    @PostMapping("/archiveByTaskId/{taskId}")
    @ApiOperation("归档任务")
    @SysLogHandler(behavior = BehaviorEnum.ARCHIVE)
    public Result archiveByTaskId(@PathVariable Integer taskId){
        workTaskService.archiveByTaskId(taskId);
        return R.ok();
    }

    @PostMapping("/uploadWorkTaskFile")
    @ApiOperation("上传任务附件")
    public Result uploadWorkTaskFile(){
        return R.ok();
    }

    @PostMapping("/archiveByTaskId")
    @ApiOperation("删除任务附件")
    public Result deleteWorkTaskFile(){
        return R.ok();
    }

    @PostMapping("/queryTrashList")
    @ApiOperation("查看回收站任务列表")
    public Result queryTrashList(){
        return R.ok(workTaskService.queryTrashList());
    }

    @PostMapping("/deleteTask/{taskId}")
    @ApiOperation("彻底删除任务")
    @SysLogHandler(behavior = BehaviorEnum.DELETE)
    public Result deleteTask(@PathVariable Integer taskId){
        workTaskService.deleteTask(taskId);
        return R.ok();
    }

    @PostMapping("/restore/{taskId}")
    @ApiOperation("还原任务")
    @SysLogHandler(behavior = BehaviorEnum.RESTORE)
    public Result restore(@PathVariable Integer taskId){
        workTaskService.restore(taskId);
        return R.ok();
    }

    @PostMapping("/queryTaskList")
    @ApiOperation("查看oa任务列表")
    public Result queryTaskList(@RequestBody OaTaskListBO oaTaskListBO){
        return R.ok(workTaskService.queryTaskList(oaTaskListBO));
    }

    @PostMapping("/workBenchTaskExport")
    @ApiOperation("导出工作台任务")
    @SysLogHandler(behavior = BehaviorEnum.EXCEL_EXPORT,object = "导出工作台任务",detail = "导出工作台任务")
    public void workBenchTaskExport(HttpServletResponse response) {
        List<Map<String, Object>> list = workTaskService.workBenchTaskExport();
        ExcelWriter writer = ExcelUtil.getWriter();
        try {
            writer.addHeaderAlias("name", "任务名称");
            writer.addHeaderAlias("description", "任务描述");
            writer.addHeaderAlias("mainUserName", "负责人");
            writer.addHeaderAlias("startTime", "开始时间");
            writer.addHeaderAlias("stopTime", "结束时间");
            writer.addHeaderAlias("labelName", "标签");
            writer.addHeaderAlias("ownerUserName", "参与人");
            writer.addHeaderAlias("priority", "优先级");
            writer.addHeaderAlias("createUserName", "创建人");
            writer.addHeaderAlias("createTime", "创建时间");
            writer.addHeaderAlias("isTop", "所在任务列表");
            writer.addHeaderAlias("relateCrmWork", "关联业务");
            writer.merge(11, "工作台任务信息");
            writer.setOnlyAlias(true);
            writer.write(list, true);
            writer.setRowHeight(0, 20);
            writer.setRowHeight(1, 20);
            for (int i = 0; i < 12; i++) {
                writer.setColumnWidth(i, 20);
            }
            Cell cell = writer.getCell(0, 0);
            CellStyle cellStyle = cell.getCellStyle();
            cellStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Font font = writer.createFont();
            font.setBold(true);
            font.setFontHeightInPoints((short) 16);
            cellStyle.setFont(font);
            cell.setCellStyle(cellStyle);
            //自定义标题别名
            //response为HttpServletResponse对象
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
            response.setHeader("Content-Disposition", "attachment;filename=myTask.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            log.error("导出工作台任务信息错误：", e);
        } finally {
            // 关闭writer，释放内存
            writer.close();
        }
    }

    @PostMapping("/updateTaskJob")
    public Result updateTaskJob(){
        workTaskService.lambdaUpdate()
                .set(WorkTask::getStatus,2)
                .eq(WorkTask::getStatus,1).apply("stop_time < now()").update();
        return Result.ok();
    }

}

