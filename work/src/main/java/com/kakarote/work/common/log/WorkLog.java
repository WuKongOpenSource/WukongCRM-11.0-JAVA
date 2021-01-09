package com.kakarote.work.common.log;

import com.kakarote.core.common.log.Content;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.core.utils.UserCacheUtil;
import com.kakarote.work.entity.BO.DeleteTaskClassBO;
import com.kakarote.work.entity.BO.RemoveWorkOwnerUserBO;
import com.kakarote.work.entity.PO.Work;
import com.kakarote.work.entity.PO.WorkTask;
import com.kakarote.work.entity.PO.WorkTaskClass;
import com.kakarote.work.service.IWorkService;
import com.kakarote.work.service.IWorkTaskClassService;
import com.kakarote.work.service.IWorkTaskService;

public class WorkLog {

    private IWorkService workService = ApplicationContextHolder.getBean(IWorkService.class);

    private IWorkTaskService workTaskService = ApplicationContextHolder.getBean(IWorkTaskService.class);

    private IWorkTaskClassService workTaskClassService = ApplicationContextHolder.getBean(IWorkTaskClassService.class);

    public Content deleteWork(Integer workId) {
        Work work = workService.getById(workId);
        return new Content(work.getName(),"删除了项目:"+work.getName());
    }

    public Content leave(Integer workId) {
        Work work = workService.getById(workId);
        return new Content(work.getName(),"退出了项目:"+work.getName());
    }

    public Content removeWorkOwnerUser(RemoveWorkOwnerUserBO workOwnerUserBO) {
        Work work = workService.getById(workOwnerUserBO.getWorkId());
        return new Content(work.getName(),"移出了项目成员:"+ UserCacheUtil.getUserName(workOwnerUserBO.getOwnerUserId()));
    }

    public Content deleteTaskList(DeleteTaskClassBO deleteTaskClassBO) {
        WorkTaskClass taskClass = workTaskClassService.getById(deleteTaskClassBO.getClassId());
        Work work = workService.getById(deleteTaskClassBO.getWorkId());
        return new Content(work.getName(),"删除了分类:"+ taskClass.getName());
    }

    public Content activation(Integer taskId) {
        WorkTask task = workTaskService.getById(taskId);
        return new Content(task.getName(),"激活已归档任务:"+ task.getName());
    }
}
