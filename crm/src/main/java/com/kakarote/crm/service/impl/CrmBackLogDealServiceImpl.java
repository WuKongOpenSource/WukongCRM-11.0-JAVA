package com.kakarote.crm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.crm.constant.CrmBackLogEnum;
import com.kakarote.crm.constant.CrmEnum;
import com.kakarote.crm.entity.PO.CrmBackLogDeal;
import com.kakarote.crm.mapper.CrmBackLogDealMapper;
import com.kakarote.crm.service.ICrmBackLogDealService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-23
 */
@Service
public class CrmBackLogDealServiceImpl extends BaseServiceImpl<CrmBackLogDealMapper, CrmBackLogDeal> implements ICrmBackLogDealService {

    /**
     * 更新对应的待办事项提醒
     *
     * @param userId         用户ID
     * @param crmEnum        类型
     * @param crmBackLogEnum 模块
     * @param typeId         类型ID
     */
    @Override
    public void deleteByType(Long userId, CrmEnum crmEnum, CrmBackLogEnum crmBackLogEnum, Integer typeId) {
        LambdaQueryWrapper<CrmBackLogDeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CrmBackLogDeal::getCreateUserId, userId);
        wrapper.eq(CrmBackLogDeal::getCrmType, crmEnum.getType());
        wrapper.eq(CrmBackLogDeal::getModel, crmBackLogEnum.getType());
        wrapper.eq(CrmBackLogDeal::getTypeId, typeId);
        remove(wrapper);
    }

    /**
     * 查询对应的主键ID
     *
     * @param model   model
     * @param crmType 类型
     * @param userId  当前用户ID
     * @return data
     */
    @Override
    public List<String> queryTypeId(Integer model, Integer crmType, Long userId) {
        List<CrmBackLogDeal> list = lambdaQuery().select(CrmBackLogDeal::getTypeId).eq(CrmBackLogDeal::getModel, model).eq(CrmBackLogDeal::getCrmType, crmType).eq(CrmBackLogDeal::getCreateUserId, userId).list();
        return list.stream().map(log -> log.getTypeId().toString()).collect(Collectors.toList());
    }
}
