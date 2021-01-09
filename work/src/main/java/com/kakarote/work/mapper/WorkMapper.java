package com.kakarote.work.mapper;

import com.kakarote.core.servlet.BaseMapper;
import com.kakarote.work.entity.BO.WorkOwnerRoleBO;
import com.kakarote.work.entity.BO.WorkTaskQueryBO;
import com.kakarote.work.entity.BO.WorkTaskTemplateBO;
import com.kakarote.work.entity.PO.Work;
import com.kakarote.work.entity.PO.WorkTask;
import com.kakarote.work.entity.VO.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 项目表 Mapper 接口
 * </p>
 *
 * @author wyq
 * @since 2020-05-15
 */
public interface WorkMapper extends BaseMapper<Work> {
    void deleteTaskRelationByWorkId(@Param("workId") Integer workId);

    void leaveTaskOwnerUser(@Param("workId") Integer workId, @Param("userId") Long userId);

    List<String> queryTaskOwnerUser(@Param("userId") Long userId);

    List<WorkInfoVo> queryWorkNameList(@Param("userId") Long userId, @Param("data") WorkTaskQueryBO workTaskQueryBO);

    List<WorkTaskTemplateClassVO> queryWorkTaskTemplateClass(@Param("taskId") Integer taskId);

    List<TaskInfoVO> queryTaskByClassId(@Param("data") WorkTaskTemplateBO workTaskTemplateBO, @Param("classId") Integer classId);

    List<TaskInfoVO> queryWorkTaskByCondition(@Param("data") WorkTaskQueryBO workTaskQueryBO, @Param("workIdList") List<Integer> workIdList);

    WorkTaskStatsVO workStatistics(@Param("workId") Integer workId, @Param("workIdList") List<Integer> workIdList, @Param("userId") Long userId, @Param("mainUserId") Long mainUserId);

    List<WorkUserStatsVO> queryAllWorkMember();

    List<Integer> queryWorkIdListByOwnerUser(@Param("userId") Long userId);

    WorkClassStatsVO queryWorkClassStats(@Param("workId") Integer workId, @Param("classId") Integer classId);

    List<WorkTask> queryWorkLabelByWorkId(@Param("workId") Integer workId);

    List<TaskInfoVO> archList(@Param("workId") Integer workId);

    public List<String> queryWorkAuthByUserId(@Param("roleId") Integer roleId, @Param("parentId") Integer parentId);

    public List<String> queryMenuByRoleId(@Param("roleId") Integer roleId);

    public List<WorkOwnerRoleBO> queryOwnerRoleList(@Param("workId") Integer workId);

    public List<Map<String, Object>> workTaskExport(@Param("workId") Integer workId);
}
