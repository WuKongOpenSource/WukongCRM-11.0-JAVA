package com.kakarote.work.service;

import com.kakarote.core.servlet.BaseService;
import com.kakarote.work.entity.PO.WorkTaskRelation;

/**
 * <p>
 * 任务关联业务表 服务类
 * </p>
 *
 * @author wyq
 * @since 2020-05-18
 */
public interface IWorkTaskRelationService extends BaseService<WorkTaskRelation> {
    public void saveWorkTaskRelation(WorkTaskRelation workTaskRelation);
}
