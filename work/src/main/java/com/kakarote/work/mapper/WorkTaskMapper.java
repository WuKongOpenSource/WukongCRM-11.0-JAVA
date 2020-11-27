package com.kakarote.work.mapper;

import cn.hutool.core.lang.Dict;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.PageEntity;
import com.kakarote.core.servlet.BaseMapper;
import com.kakarote.work.entity.BO.OaTaskListBO;
import com.kakarote.work.entity.BO.TaskLabelBO;
import com.kakarote.work.entity.BO.WorkTaskLabelBO;
import com.kakarote.work.entity.BO.WorkTaskNameBO;
import com.kakarote.work.entity.PO.WorkTask;
import com.kakarote.work.entity.VO.OaTaskListVO;
import com.kakarote.work.entity.VO.TaskDetailVO;
import com.kakarote.work.entity.VO.TaskInfoVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 任务表 Mapper 接口
 * </p>
 *
 * @author wyq
 * @since 2020-05-18
 */
public interface WorkTaskMapper extends BaseMapper<WorkTask> {

    List<TaskInfoVO> queryMyTaskList(@Param("isTop")Integer isTop,@Param("userId")Long userId,@Param("data")WorkTaskNameBO workTaskNameBO);

    BasePage<TaskInfoVO> queryMyTaskList(PageEntity page, @Param("isTop")Integer isTop, @Param("userId")Long userId );

    List<TaskLabelBO> queryTaskLabel(@Param("list")List<Integer> taskIdList);

    TaskDetailVO queryTaskInfo(@Param("taskId")Integer taskId);

    List<WorkTaskLabelBO> queryWorkTaskLabelList(@Param("list")List<Integer> labelIdList);

    List<TaskInfoVO> queryTrashList(@Param("userId")Long userId);

    OaTaskListVO queryTaskCount(@Param("data")OaTaskListBO oaTaskListBO, @Param("userIdList")List<Long> userIdList);

    BasePage<TaskInfoVO> getTaskList(BasePage<TaskInfoVO> page,@Param("data")OaTaskListBO oaTaskListBO, @Param("userIdList")List<Long> userIdList);

    List<JSONObject> getTaskListExport(@Param("data")OaTaskListBO oaTaskListBO,@Param("userIdList") List<Long> userIds);

    List<JSONObject> myTaskExport(@Param("data") Dict kv);
}
