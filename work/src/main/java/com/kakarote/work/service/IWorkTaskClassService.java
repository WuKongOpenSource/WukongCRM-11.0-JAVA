package com.kakarote.work.service;

import com.kakarote.core.servlet.BaseService;
import com.kakarote.work.entity.PO.WorkTaskClass;

import java.util.List;

/**
 * <p>
 * 任务分类表 服务类
 * </p>
 *
 * @author wyq
 * @since 2020-05-15
 */
public interface IWorkTaskClassService extends BaseService<WorkTaskClass> {
    public List<WorkTaskClass> queryClassNameByWorkId(Integer workId);

    public void saveWorkTaskClass(WorkTaskClass workTaskClass);

    public void updateWorkTaskClass(WorkTaskClass workTaskClass);
}
