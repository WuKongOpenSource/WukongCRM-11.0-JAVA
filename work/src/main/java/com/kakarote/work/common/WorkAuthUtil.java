package com.kakarote.work.common;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.utils.TagUtil;
import com.kakarote.core.utils.UserCacheUtil;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.work.entity.PO.Work;
import com.kakarote.work.entity.PO.WorkTask;
import com.kakarote.work.entity.PO.WorkTaskComment;
import com.kakarote.work.service.IWorkService;
import com.kakarote.work.service.IWorkTaskCommentService;
import com.kakarote.work.service.IWorkTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Set;


/**
 * @author wyq
 */
@Component
public class WorkAuthUtil {
    @Autowired
    private IWorkService workService;

    @Autowired
    private IWorkTaskService workTaskService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private IWorkTaskCommentService workTaskCommentService;

    public boolean isWorkAdmin() {
        if (UserUtil.getUserId().equals(UserUtil.getSuperUser())) {
            return true;
        } else {
            return UserUtil.getUser().getRoles().contains(getWorkAdminRole()) || UserUtil.getUser().getRoles().contains(UserUtil.getSuperRole());
        }
    }


    public Integer getWorkAdminRole() {
        return adminService.queryWorkRole(1).getData();
    }

    public Integer getSmallAdminRole() {
        return adminService.queryWorkRole(2).getData();
    }

    public Integer getSmallEditRole() {
        return adminService.queryWorkRole(3).getData();
    }

    /**
     * 判断当前用户是否有查看任务的权限
     *
     * @param taskId
     * @return
     */
    public boolean isTaskAuth(Integer taskId) {
        WorkTask workTask = workTaskService.getById(taskId);
        if (ObjectUtil.isNotEmpty(workTask) && ObjectUtil.equal(workTask.getWorkId(), 0)) {
            if (UserUtil.isAdmin()) {
                return true;
            } else {
                Long userId = UserUtil.getUserId();
                List<Long> subUserIdList = UserCacheUtil.queryChildUserId(userId);
                Set<Long> ownerUserIdList = TagUtil.toLongSet(workTask.getOwnerUserId());
                subUserIdList.retainAll(ownerUserIdList);
                return ObjectUtil.equal(workTask.getMainUserId(), userId) || ownerUserIdList.contains(userId) || subUserIdList.size() > 0;
            }
        }
        return isWorkAuth(workTask.getWorkId());
    }

    public boolean isWorkAuth(Integer workId) {
        if (isWorkAdmin()) {
            return true;
        }
        Long userId = UserUtil.getUserId();
        Work work = workService.getById(workId);
        if (ObjectUtil.isNotEmpty(work)) {
            return work.getIsOpen() == 1 || work.getOwnerUserId().contains("," + userId + ",");
        }
        return true;
    }

    /**
     * 查询是否有权限
     *
     * @param type 1 任务 2 评论
     * @param id   id
     * @return bool
     */
    public boolean isOaAuth(Integer type, Integer id) {
        if (id == null) {
            return false;
        }
        Long userId = UserUtil.getUserId();
        //超级管理员角色拥有最高权限
        if (UserUtil.isAdmin()) {
            return false;
        }
        if (Objects.equals(1, type)) {
            if (UserUtil.getUser().getRoles().contains(getWorkAdminRole())) {
                return false;
            }
            List<Long> childIdList = adminService.queryChildUserId(userId).getData();
            childIdList.add(userId);
            LambdaQueryChainWrapper<WorkTask> chainWrapper = workTaskService.lambdaQuery();
            chainWrapper.eq(WorkTask::getTaskId, id);
            if (childIdList.size() > 0) {
                chainWrapper.and(wrapper->{
                    childIdList.forEach(childId -> {
                        wrapper.or(child->{
                            child.like(WorkTask::getOwnerUserId, childId);
                            child.or(or->or.eq(WorkTask::getMainUserId, childId));
                        });
                    });
                });
            } else {
                chainWrapper.like(WorkTask::getOwnerUserId, userId);
                chainWrapper.or(wrapper -> {
                    wrapper.eq(WorkTask::getMainUserId, userId);
                });
            }
            return chainWrapper.count() == 0;
        } else if (Objects.equals(2, type)) {
            LambdaQueryChainWrapper<WorkTaskComment> chainWrapper = workTaskCommentService.lambdaQuery();
            chainWrapper.eq(WorkTaskComment::getCommentId, id);
            chainWrapper.eq(WorkTaskComment::getUserId, userId);
            return chainWrapper.count() == 0;
        } else {
            return false;
        }
    }
}
