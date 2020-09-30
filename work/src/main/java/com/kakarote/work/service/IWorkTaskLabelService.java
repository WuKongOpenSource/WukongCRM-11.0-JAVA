package com.kakarote.work.service;

import com.kakarote.core.servlet.BaseService;
import com.kakarote.work.entity.PO.WorkTaskLabel;
import com.kakarote.work.entity.VO.WorkTaskByLabelVO;
import com.kakarote.work.entity.VO.WorkTaskLabelOrderVO;

import java.util.List;

/**
 * <p>
 * 任务标签表 服务类
 * </p>
 *
 * @author wyq
 * @since 2020-05-15
 */
public interface IWorkTaskLabelService extends BaseService<WorkTaskLabel> {
    public void saveLabel(WorkTaskLabel workTaskLabel);

    public void setLabel(WorkTaskLabel workTaskLabel);

    public void deleteLabel(Integer labelId);

    public List<WorkTaskLabelOrderVO> getLabelList();

    public void updateOrder(List<Integer> labelIdList);

    public WorkTaskLabel queryById(Integer labelId);

    public List<WorkTaskByLabelVO> getTaskList(Integer labelId);
}
