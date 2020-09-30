package com.kakarote.work.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.work.common.WorkCodeEnum;
import com.kakarote.work.entity.PO.WorkTask;
import com.kakarote.work.entity.PO.WorkTaskLabel;
import com.kakarote.work.entity.PO.WorkTaskLabelOrder;
import com.kakarote.work.entity.VO.TaskInfoVO;
import com.kakarote.work.entity.VO.WorkTaskByLabelVO;
import com.kakarote.work.entity.VO.WorkTaskLabelOrderVO;
import com.kakarote.work.mapper.WorkTaskLabelMapper;
import com.kakarote.work.service.IWorkTaskLabelOrderService;
import com.kakarote.work.service.IWorkTaskLabelService;
import com.kakarote.work.service.IWorkTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 任务标签表 服务实现类
 * </p>
 *
 * @author wyq
 * @since 2020-05-15
 */
@Service
public class WorkTaskLabelServiceImpl extends BaseServiceImpl<WorkTaskLabelMapper, WorkTaskLabel> implements IWorkTaskLabelService {
    @Autowired
    private IWorkTaskService workTaskService;

    @Autowired
    private IWorkTaskLabelOrderService workTaskLabelOrderService;

    @Override
    public void saveLabel(WorkTaskLabel taskLabel) {
        save(taskLabel);
    }

    @Override
    public void setLabel(WorkTaskLabel workTaskLabel) {
        updateById(workTaskLabel);
    }

    @Override
    public void deleteLabel(Integer labelId) {
        Integer count = workTaskService.count(new QueryWrapper<WorkTask>().like("label_id",labelId));
        if(count>0){
            throw new CrmException(WorkCodeEnum.WORK_LABEL_DELETE_ERROR);
        }
        removeById(labelId);
    }

    @Override
    public List<WorkTaskLabelOrderVO> getLabelList() {
        Long userId = UserUtil.getUserId();
        List<WorkTaskLabelOrder> orderList = workTaskLabelOrderService.list(new QueryWrapper<WorkTaskLabelOrder>().select("label_id","order_num").eq("user_id",userId));
        List<WorkTaskLabelOrderVO> labelList = getBaseMapper().getLabelList();
        labelList.forEach(label -> {
            orderList.forEach(order->{
                if (order.getLabelId().equals(label.getLabelId())){
                    label.setOrderNum(order.getOrderNum());
                }
            });
        });
        return labelList.stream().sorted(Comparator.comparingInt(WorkTaskLabelOrderVO::getOrderNum)).collect(Collectors.toList());
    }

    @Override
    public void updateOrder(List<Integer> labelIdList) {
        Long userId = UserUtil.getUserId();
        workTaskLabelOrderService.remove(new QueryWrapper<WorkTaskLabelOrder>().eq("user_id",userId));
        for (int i = 0; i < labelIdList.size(); i++) {
            WorkTaskLabelOrder workTaskLabelOrder = new WorkTaskLabelOrder();
            workTaskLabelOrder.setLabelId(labelIdList.get(i));
            workTaskLabelOrder.setUserId(userId);
            workTaskLabelOrder.setOrderNum(i+1);
            workTaskLabelOrderService.save(workTaskLabelOrder);
        }
    }

    @Override
    public WorkTaskLabel queryById(Integer labelId) {
        return getById(labelId);
    }

    @Override
    public List<WorkTaskByLabelVO> getTaskList(Integer labelId) {
        List<TaskInfoVO> taskList = getBaseMapper().getTaskList(labelId,UserUtil.getUserId());
        workTaskService.taskListTransfer(taskList);
        Map<Integer,List<TaskInfoVO>> map = taskList.stream().collect(Collectors.groupingBy(TaskInfoVO::getWorkId));
        List<WorkTaskByLabelVO> workList = getBaseMapper().getWorkList(labelId,UserUtil.getUserId());
        workList.forEach(work -> work.setList(map.get(work.getWorkId())));
        return workList;
    }
}
