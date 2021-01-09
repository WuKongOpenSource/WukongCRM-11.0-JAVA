package com.kakarote.work.service;

import com.kakarote.core.servlet.BaseService;
import com.kakarote.work.entity.BO.*;
import com.kakarote.work.entity.PO.WorkTask;
import com.kakarote.work.entity.VO.MyTaskVO;
import com.kakarote.work.entity.VO.OaTaskListVO;
import com.kakarote.work.entity.VO.TaskDetailVO;
import com.kakarote.work.entity.VO.TaskInfoVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 任务表 服务类
 * </p>
 *
 * @author wyq
 * @since 2020-05-18
 */
public interface IWorkTaskService extends BaseService<WorkTask> {

    public List<MyTaskVO> myTask(WorkTaskNameBO workTaskNameBO,boolean isInternal);

    public void taskListTransfer(List<TaskInfoVO> taskList);

    public void updateTop(UpdateTaskTopBo updateTaskClassBo);

    public void saveWorkTask(WorkTask workTask);

    public boolean updateWorkTask(WorkTask task);

    public void setWorkTaskStatus(WorkTaskStatusBO workTaskStatusBO);

    public void setWorkTaskTitle(WorkTaskNameBO workTaskNameBO);

    public void setWorkTaskDescription(WorkTaskDescriptionBO workTaskDescriptionBO);

    public void setWorkTaskMainUser(WorkTaskUserBO workTaskUserBO);

    public void setWorkTaskOwnerUser(WorkTaskOwnerUserBO workTaskOwnerUserBO);

    public void setWorkTaskTime(WorkTask workTask);

    public void setWorkTaskLabel(WorkTaskLabelsBO workTaskLabelsBO);

    public void setWorkTaskPriority(WorkTaskPriorityBO workTaskPriorityBO);

    public WorkTask addWorkChildTask(WorkTask workTask);

    public void updateWorkChildTask(WorkTask workTask);

    public void setWorkChildTaskStatus(WorkTaskStatusBO workTaskStatusBO);

    public void deleteWorkTaskOwnerUser(WorkTaskUserBO workTaskUserBO);

    public void deleteWorkTaskLabel(WorkTaskLabelBO workTaskLabelBO);

    public TaskDetailVO queryTaskInfo(Integer taskId);

    public TaskDetailVO queryTrashTaskInfo(Integer taskId);

    public void deleteWorkTask(Integer taskId);

    public void archiveByTaskId(Integer taskId);

    public List<TaskInfoVO> queryTrashList();

    public void deleteTask(Integer taskId);

    public void restore(Integer taskId);

    public OaTaskListVO queryTaskList(OaTaskListBO oaTaskListBO);

    List<Map<String, Object>> workBenchTaskExport();


    public String getPriorityDesc(Integer priority);

}
