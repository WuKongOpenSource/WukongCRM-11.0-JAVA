package com.kakarote.work.mapper;

import com.kakarote.core.servlet.BaseMapper;
import com.kakarote.work.entity.PO.WorkTaskLabel;
import com.kakarote.work.entity.VO.TaskInfoVO;
import com.kakarote.work.entity.VO.WorkTaskByLabelVO;
import com.kakarote.work.entity.VO.WorkTaskLabelOrderVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 任务标签表 Mapper 接口
 * </p>
 *
 * @author wyq
 * @since 2020-05-15
 */
public interface WorkTaskLabelMapper extends BaseMapper<WorkTaskLabel> {
    List<WorkTaskLabelOrderVO> getLabelList();

    List<TaskInfoVO> getTaskList(@Param("labelId")Integer labelId, @Param("userId")Long userId);

    List<WorkTaskByLabelVO> getWorkList(@Param("labelId")Integer labelId, @Param("userId")Long userId);
}
