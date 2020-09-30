package com.kakarote.work.service;

import com.kakarote.core.servlet.BaseService;
import com.kakarote.work.entity.PO.WorkTaskComment;

import java.util.List;

/**
 * <p>
 * 任务评论表 服务类
 * </p>
 *
 * @author wyq
 * @since 2020-05-18
 */
public interface IWorkTaskCommentService extends BaseService<WorkTaskComment> {

    /**
     * 查询评论列表
     * @param typeId typeId
     * @param type type
     * @return data
     */
    public List<WorkTaskComment> queryCommentList(Integer typeId, Integer type);

    /**
     * 新增评论
     * @param taskComment taskComment
     */
    public void setComment(WorkTaskComment taskComment);
}
