package com.kakarote.work.common.log;
import com.kakarote.core.common.log.Content;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.work.entity.PO.WorkTaskLabel;
import com.kakarote.work.service.IWorkTaskLabelService;

public class WorkTaskLabelLog {
    private IWorkTaskLabelService workTaskLabelService = ApplicationContextHolder.getBean(IWorkTaskLabelService.class);

    public Content deleteLabel(Integer labelId){
        WorkTaskLabel taskLabel = workTaskLabelService.getById(labelId);
        return new Content(taskLabel.getName(),"删除了标签"+taskLabel.getName());
    }
}
