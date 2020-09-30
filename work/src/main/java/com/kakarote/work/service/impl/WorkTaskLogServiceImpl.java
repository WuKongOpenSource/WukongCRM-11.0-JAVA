package com.kakarote.work.service.impl;

import cn.hutool.core.date.DateUtil;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.work.common.WorkAuthUtil;
import com.kakarote.work.common.WorkCodeEnum;
import com.kakarote.work.entity.PO.WorkTaskLog;
import com.kakarote.work.entity.VO.WorkTaskLogVO;
import com.kakarote.work.mapper.WorkTaskLogMapper;
import com.kakarote.work.service.IWorkTaskLogService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 任务日志表 服务实现类
 * </p>
 *
 * @author wyq
 * @since 2020-05-15
 */
@Service
public class WorkTaskLogServiceImpl extends BaseServiceImpl<WorkTaskLogMapper, WorkTaskLog> implements IWorkTaskLogService {
    @Override
    public void saveWorkTaskLog(WorkTaskLog workTaskLog) {
        workTaskLog.setCreateTime(DateUtil.date());
        workTaskLog.setLogId(null);
        save(workTaskLog);
    }

    @Override
    public List<WorkTaskLogVO> queryTaskLog(Integer taskId) {
        WorkAuthUtil workAuthUtil = ApplicationContextHolder.getBean(WorkAuthUtil.class);
        if (!workAuthUtil.isWorkAuth(taskId)){
            throw new CrmException(WorkCodeEnum.WORK_AUTH_ERROR);
        }
        return getBaseMapper().queryTaskLog(taskId);
    }
}
