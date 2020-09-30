package com.kakarote.work.controller;


import com.kakarote.core.common.R;
import com.kakarote.core.common.Result;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.utils.TagUtil;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.work.common.WorkAuthUtil;
import com.kakarote.work.common.WorkCodeEnum;
import com.kakarote.work.entity.PO.Work;
import com.kakarote.work.entity.PO.WorkTask;
import com.kakarote.work.entity.PO.WorkTaskComment;
import com.kakarote.work.service.IWorkService;
import com.kakarote.work.service.IWorkTaskCommentService;
import com.kakarote.work.service.IWorkTaskService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 任务评论表 前端控制器
 * </p>
 *
 * @author wyq
 * @since 2020-05-18
 */
@RestController
@RequestMapping("/workTaskComment")
public class WorkTaskCommentController {

    @Autowired
    private WorkAuthUtil workAuthUtil;

    @Autowired
    private IWorkTaskService workTaskService;

    @Autowired
    private IWorkService workService;

    @Autowired
    private IWorkTaskCommentService workTaskCommentService;

    @ApiOperation("查询评论列表")
    @PostMapping("/queryCommentList")
    public Result<List<WorkTaskComment>> queryCommentList(@ApiParam("类型ID") @RequestParam("typeId") Integer typeId, @ApiParam("类型") @RequestParam("type") Integer type) {
        if (!UserUtil.isAdmin() && !UserUtil.getUser().getRoles().contains(workAuthUtil.getWorkAdminRole())) {
            if (Objects.equals(1, type)) {
                WorkTask task = workTaskService.getById(typeId);
                boolean auth = true;
                if (Objects.equals(0, task.getWorkId())) {
                    auth = workAuthUtil.isOaAuth(1, typeId);
                } else {
                    Work work = workService.getById(task.getWorkId());
                    if (TagUtil.toLongSet(work.getOwnerUserId()).contains(UserUtil.getUserId()) || work.getIsOpen() == 1) {
                        auth = false;
                    }
                }
                if (auth) {
                    return R.error(SystemCodeEnum.SYSTEM_NO_AUTH);
                }
            }
        }
        List<WorkTaskComment> taskComments = workTaskCommentService.queryCommentList(typeId, type);
        return R.ok(taskComments);
    }

    /**
     * @param comment 评论对象
     * @author hmb
     * 添加评论或者修改
     */
    @PostMapping("/setComment")
    @ApiOperation("添加评论或者修改")
    public Result setComment(@RequestBody WorkTaskComment comment) {
        if (comment.getType() == 1) {
            if (!workAuthUtil.isTaskAuth(comment.getTypeId())) {
                throw new CrmException(WorkCodeEnum.WORK_AUTH_ERROR);
            }
        }
        workTaskCommentService.setComment(comment);
        return R.ok(comment);
    }

    @PostMapping("/deleteComment")
    @ApiOperation("删除评论")
    public Result deleteComment(@RequestParam("commentId") Integer commentId) {
        WorkTaskComment comment = workTaskCommentService.getById(commentId);
        if (comment != null) {
            if (comment.getType() == 1) {
                if (!workAuthUtil.isTaskAuth(comment.getTypeId())) {
                    throw new CrmException(WorkCodeEnum.WORK_AUTH_ERROR);
                }
            }
            workTaskCommentService.removeById(commentId);
            workTaskCommentService.lambdaUpdate().eq(WorkTaskComment::getMainId,commentId).remove();
        }
        return R.ok();
    }
}

