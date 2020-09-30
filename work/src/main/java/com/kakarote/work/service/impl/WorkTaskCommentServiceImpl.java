package com.kakarote.work.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.kakarote.core.feign.admin.entity.AdminMessageBO;
import com.kakarote.core.feign.admin.entity.AdminMessageEnum;
import com.kakarote.core.feign.admin.entity.SimpleUser;
import com.kakarote.core.feign.admin.service.AdminMessageService;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.work.entity.PO.WorkTaskComment;
import com.kakarote.work.mapper.WorkTaskCommentMapper;
import com.kakarote.work.service.IWorkTaskCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 任务评论表 服务实现类
 * </p>
 *
 * @author wyq
 * @since 2020-05-18
 */
@Service
public class WorkTaskCommentServiceImpl extends BaseServiceImpl<WorkTaskCommentMapper, WorkTaskComment> implements IWorkTaskCommentService {

    @Autowired
    private AdminService adminService;

    /**
     * 查询评论列表
     *
     * @param typeId typeId
     * @param type   type
     * @return data
     */
    @Override
    public List<WorkTaskComment> queryCommentList(Integer typeId, Integer type) {
        LambdaQueryChainWrapper<WorkTaskComment> chainWrapper = lambdaQuery();
        chainWrapper.eq(WorkTaskComment::getType, type);
        chainWrapper.eq(WorkTaskComment::getTypeId, typeId);
        chainWrapper.orderByDesc(WorkTaskComment::getCreateTime);
        List<WorkTaskComment> taskCommentList = chainWrapper.list();
        if (taskCommentList == null || taskCommentList.size() == 0) {
            return new ArrayList<>();
        }
        taskCommentList.forEach(record -> {
            if (record.getUserId() != null) {
                List<SimpleUser> data = adminService.queryUserByIds(Collections.singleton(record.getUserId())).getData();
                record.setUser(data.size() > 0 ? data.get(0) : null);
            }
            if (!Objects.equals(0L, record.getPid())) {
                List<SimpleUser> data = adminService.queryUserByIds(Collections.singleton(record.getPid())).getData();
                record.setReplyUser(data.size() > 0 ? data.get(0) : null);
            }
        });
        Map<Integer, List<WorkTaskComment>> pMap = taskCommentList.stream().collect(Collectors.groupingBy(WorkTaskComment::getMainId));
        taskCommentList = pMap.get(0);
        taskCommentList.forEach(record -> {
            Integer commentId = record.getCommentId();
            if (pMap.get(commentId) != null) {
                record.setChildCommentList(pMap.get(commentId));
            } else {
                record.setChildCommentList(new ArrayList<>());
            }
        });
        return taskCommentList;
    }

    /**
     * 新增评论
     *
     * @param comment taskComment
     */
    @Override
    public void setComment(WorkTaskComment comment) {
        if (comment.getCommentId() == null) {
            comment.setCreateTime(new Date());
            comment.setUserId(UserUtil.getUserId());
            save(comment);
            if (comment.getType().equals(2)) {
                AdminMessageBO adminMessageBO = new AdminMessageBO();
                JSONObject object = getBaseMapper().queryOaLog(comment.getTypeId());
                adminMessageBO.setUserId(comment.getUserId());
                adminMessageBO.setTitle(DateUtil.formatDate(object.getDate("createTime")));
                adminMessageBO.setContent(comment.getContent());
                adminMessageBO.setTypeId(comment.getTypeId());
                if (comment.getMainId() != null && !comment.getMainId().equals(0)) {
                    adminMessageBO.setMessageType(AdminMessageEnum.OA_COMMENT_REPLY.getType());
                    adminMessageBO.setIds(Collections.singletonList(comment.getPid()));
                } else {
                    adminMessageBO.setMessageType(AdminMessageEnum.OA_LOG_REPLY.getType());
                    adminMessageBO.setIds(Collections.singletonList(object.getLong("createUserId")));
                }
                ApplicationContextHolder.getBean(AdminMessageService.class).sendMessage(adminMessageBO);
            }
        } else {
            updateById(comment);
        }
    }
}
