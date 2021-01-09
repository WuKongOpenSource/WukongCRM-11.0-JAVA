package com.kakarote.work.common.log;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.kakarote.core.common.log.Content;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.core.utils.UserCacheUtil;
import com.kakarote.work.entity.BO.*;
import com.kakarote.work.entity.PO.WorkTask;
import com.kakarote.work.entity.PO.WorkTaskLabel;
import com.kakarote.work.service.IWorkTaskLabelService;
import com.kakarote.work.service.IWorkTaskService;

import java.util.ArrayList;
import java.util.List;

public class WorkTaskLog {

    private IWorkTaskService workTaskService = ApplicationContextHolder.getBean(IWorkTaskService.class);

    private IWorkTaskLabelService workTaskLabelService = ApplicationContextHolder.getBean(IWorkTaskLabelService.class);

    public Content setWorkTaskStatus(WorkTaskStatusBO workTaskStatusBO) {
        WorkTask workTask = workTaskService.getById(workTaskStatusBO.getTaskId());
        String statusName = "";
        if (workTaskStatusBO.getStatus().equals(1)) {
            statusName = "未完成";
        } else if (workTaskStatusBO.getStatus().equals(5)) {
            statusName = "完成";
        }
        return new Content(workTask.getName(), "将状态修改为:" + statusName);
    }

    public Content setWorkTaskTitle(WorkTaskNameBO workTaskNameBO) {
        WorkTask workTask = workTaskService.getById(workTaskNameBO.getTaskId());
        return new Content(workTask.getName(), "修改任务标题:" + workTaskNameBO.getName());
    }

    public Content setWorkTaskDescription(WorkTaskDescriptionBO workTaskDescriptionBO) {
        WorkTask workTask = workTaskService.getById(workTaskDescriptionBO.getTaskId());
        return new Content(workTask.getName(), "修改任务描述:" + workTaskDescriptionBO.getDescription());
    }

    public Content setWorkTaskMainUser(WorkTaskUserBO workTaskUserBO) {
        WorkTask workTask = workTaskService.getById(workTaskUserBO.getTaskId());
        return new Content(workTask.getName(), "修改任务负责人为:" + UserCacheUtil.getUserName(workTaskUserBO.getUserId()));
    }

    public Content setWorkTaskOwnerUser(WorkTaskOwnerUserBO workTaskOwnerUserBO) {
        List<String> userNames = new ArrayList<>();
        for (String userId : workTaskOwnerUserBO.getOwnerUserId().split(",")) {
            userNames.add(UserCacheUtil.getUserName(Long.valueOf(userId)));
        }
        WorkTask workTask = workTaskService.getById(workTaskOwnerUserBO.getTaskId());
        return new Content(workTask.getName(), "修改任务参与人为:" + CollUtil.join(userNames, ","));
    }

    public Content setWorkTaskTime(WorkTask workTask) {
        WorkTask workTask1 = workTaskService.getById(workTask.getTaskId());
        String detail = "";
        if (workTask.getStartTime() != null) {
            detail += "修改任务开始时间为:" + DateUtil.formatDate(workTask.getStartTime()) + "。";
        }
        if (workTask.getStopTime() != null) {
            detail += "修改任务结束时间为:" + DateUtil.formatDate(workTask.getStopTime()) + "。";
        }
        return new Content(workTask1.getName(), detail);
    }

    public Content setWorkTaskLabel(WorkTaskLabelsBO workTaskLabelsBO) {
        List<String> labelNames = new ArrayList<>();
        for (String labelId : workTaskLabelsBO.getLabelId().split(",")) {
            WorkTaskLabel taskLabel = workTaskLabelService.getById(workTaskLabelsBO.getLabelId());
            labelNames.add(taskLabel.getName());
        }
        WorkTask workTask = workTaskService.getById(workTaskLabelsBO.getTaskId());
        return new Content(workTask.getName(), "修改任务标签为:" + CollUtil.join(labelNames, ","));
    }

    public Content setWorkTaskPriority(WorkTaskPriorityBO workTaskPriorityBO) {
        WorkTask workTask = workTaskService.getById(workTaskPriorityBO.getTaskId());
        return new Content(workTask.getName(), "修改任务优先级为:" + workTaskService.getPriorityDesc(workTaskPriorityBO.getPriority()));
    }

    public Content addWorkChildTask(WorkTask workTask){
        WorkTask workTask1 = workTaskService.getById(workTask.getPid());
        return new Content(workTask1.getName(), "添加了子任务:" + workTask.getName());
    }

    public Content deleteWorkTaskLabel(WorkTaskLabelBO workTaskLabelBO){
        WorkTaskLabel taskLabel = workTaskLabelService.getById(workTaskLabelBO.getLabelId());
        WorkTask workTask = workTaskService.getById(workTaskLabelBO.getTaskId());
        return new Content(workTask.getName(), "删除了标签:" + taskLabel.getName());
    }

    public Content deleteWorkTask(Integer taskId){
        WorkTask workTask = workTaskService.getById(taskId);
        return new Content(workTask.getName(), "删除了任务:" + workTask.getName());
    }

    public Content deleteWorkChildTask(Integer taskId){
        WorkTask workTask = workTaskService.getById(taskId);
        return new Content(workTask.getName(), "删除了子任务:" + workTask.getName());
    }

    public Content archiveByTaskId(Integer taskId){
        WorkTask workTask = workTaskService.getById(taskId);
        return new Content(workTask.getName(), "归档了任务:" + workTask.getName());
    }

    public Content deleteTask(Integer taskId){
        WorkTask workTask = workTaskService.getById(taskId);
        return new Content(workTask.getName(), "彻底删除任务:" + workTask.getName());
    }

    public Content restore(Integer taskId){
        WorkTask workTask = workTaskService.getById(taskId);
        return new Content(workTask.getName(), "从回收站恢复了任务:" + workTask.getName());
    }
}
