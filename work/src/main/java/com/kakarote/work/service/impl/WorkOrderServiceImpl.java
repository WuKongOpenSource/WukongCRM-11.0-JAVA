package com.kakarote.work.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.work.entity.PO.WorkOrder;
import com.kakarote.work.mapper.WorkOrderMapper;
import com.kakarote.work.service.IWorkOrderService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 项目排序表 服务实现类
 * </p>
 *
 * @author wyq
 * @since 2020-05-15
 */
@Service
public class WorkOrderServiceImpl extends BaseServiceImpl<WorkOrderMapper, WorkOrder> implements IWorkOrderService {

    @Override
    public void updateWorkOrder(List<Integer> workIdList) {
        Long userId = UserUtil.getUserId();
        remove(new QueryWrapper<WorkOrder>().eq("user_id",userId));
        for (int i = 0; i < workIdList.size(); i++) {
            WorkOrder workOrder = new WorkOrder();
            workOrder.setWorkId(workIdList.get(i));
            workOrder.setUserId(userId);
            workOrder.setOrderNum(i + 1);
            save(workOrder);
        }
    }
}
