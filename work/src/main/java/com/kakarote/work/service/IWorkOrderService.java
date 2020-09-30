package com.kakarote.work.service;

import com.kakarote.core.servlet.BaseService;
import com.kakarote.work.entity.PO.WorkOrder;

import java.util.List;

/**
 * <p>
 * 项目排序表 服务类
 * </p>
 *
 * @author wyq
 * @since 2020-05-15
 */
public interface IWorkOrderService extends BaseService<WorkOrder> {
    public void updateWorkOrder(List<Integer> workIdList);
}
