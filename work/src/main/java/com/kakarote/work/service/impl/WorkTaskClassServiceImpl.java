package com.kakarote.work.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.work.common.WorkCodeEnum;
import com.kakarote.work.entity.PO.WorkTaskClass;
import com.kakarote.work.mapper.WorkTaskClassMapper;
import com.kakarote.work.service.IWorkTaskClassService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 任务分类表 服务实现类
 * </p>
 *
 * @author wyq
 * @since 2020-05-15
 */
@Service
public class WorkTaskClassServiceImpl extends BaseServiceImpl<WorkTaskClassMapper, WorkTaskClass> implements IWorkTaskClassService {

    @Override
    public List<WorkTaskClass> queryClassNameByWorkId(Integer workId) {
        return getBaseMapper().selectList(new QueryWrapper<WorkTaskClass>().select("class_id","name").eq("work_id",workId));
    }

    @Override
    public void saveWorkTaskClass(WorkTaskClass workTaskClass) {
        Integer orderNum = query().select("max(order_num) as order_num").eq("work_id",workTaskClass.getWorkId()).oneOpt().map(WorkTaskClass::getOrderNum).orElse(0);
        workTaskClass.setOrderNum(orderNum + 1);
        workTaskClass.setCreateUserId(UserUtil.getUserId());
        workTaskClass.setCreateTime(new Date());
        save(workTaskClass);
    }

    @Override
    public void updateWorkTaskClass(WorkTaskClass workTaskClass) {
        Integer workId = getOne(new QueryWrapper<WorkTaskClass>().eq("class_id",workTaskClass.getClassId())).getWorkId();
        if (ObjectUtil.isEmpty(workId)) {
            throw new CrmException(WorkCodeEnum.WORK_EXIST_ERROR);
        }
        updateById(workTaskClass);
    }

}
