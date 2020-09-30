package com.kakarote.work.mapper;

import com.kakarote.core.servlet.BaseMapper;
import com.kakarote.work.entity.PO.WorkTaskLog;
import com.kakarote.work.entity.VO.WorkTaskLogVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 任务日志表 Mapper 接口
 * </p>
 *
 * @author wyq
 * @since 2020-05-15
 */
public interface WorkTaskLogMapper extends BaseMapper<WorkTaskLog> {
    List<WorkTaskLogVO> queryTaskLog(@Param("taskId")Integer taskId);
}
