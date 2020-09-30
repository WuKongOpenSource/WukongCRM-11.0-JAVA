package com.kakarote.work.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.core.feign.crm.service.CrmService;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.work.entity.PO.WorkTaskRelation;
import com.kakarote.work.mapper.WorkTaskRelationMapper;
import com.kakarote.work.service.IWorkTaskRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 任务关联业务表 服务实现类
 * </p>
 *
 * @author wyq
 * @since 2020-05-18
 */
@Service
public class WorkTaskRelationServiceImpl extends BaseServiceImpl<WorkTaskRelationMapper, WorkTaskRelation> implements IWorkTaskRelationService {
    @Autowired
    private CrmService crmService;
    @Override
    public void saveWorkTaskRelation(WorkTaskRelation workTaskRelation) {
        if (workTaskRelation.getBusinessIds() != null || workTaskRelation.getContactsIds() != null || workTaskRelation.getContractIds() != null || workTaskRelation.getCustomerIds() != null) {
            crmService.addActivity(2, 11, workTaskRelation.getTaskId());
        }
        remove(new QueryWrapper<WorkTaskRelation>().eq("task_id",workTaskRelation.getTaskId()));
        save(workTaskRelation);
    }
}
