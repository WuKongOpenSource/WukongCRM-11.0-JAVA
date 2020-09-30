package com.kakarote.oa.controller;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.kakarote.core.common.Result;
import com.kakarote.oa.constart.entity.BO.*;
import com.kakarote.oa.constart.entity.PO.WorkTask;
import com.kakarote.oa.constart.entity.VO.OaTaskListVO;
import com.kakarote.oa.service.IOaTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
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
            writer.addHeaderAlias("relateCrmWork", "关联业务");
            writer.merge(10, "办公任务信息");
            writer.setOnlyAlias(true);
            writer.write(list, true);
            writer.setRowHeight(0, 20);
            writer.setRowHeight(1, 20);
            for (int i = 0; i < 11; i++) {
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
            response.setHeader("Content-Disposition", "attachment;filename=OaTask.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭writer，释放内存
            writer.close();
        }
    }

}
