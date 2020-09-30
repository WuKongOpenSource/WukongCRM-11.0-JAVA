package com.kakarote.crm.service;

import com.kakarote.core.servlet.BaseService;
import com.kakarote.crm.constant.CrmBackLogEnum;
import com.kakarote.crm.constant.CrmEnum;
import com.kakarote.crm.entity.PO.CrmBackLogDeal;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-23
 */
public interface ICrmBackLogDealService extends BaseService<CrmBackLogDeal> {
    /**
     * 更新对应的待办事项提醒
     *
     * @param userId         用户ID
     * @param crmEnum        类型
     * @param crmBackLogEnum 模块
     * @param typeId         类型ID
     */
    public void deleteByType(Long userId, CrmEnum crmEnum, CrmBackLogEnum crmBackLogEnum, Integer typeId);

    /**
     * 查询对应的主键ID
     * @param model model
     * @param crmType 类型
     * @param userId 当前用户ID
     * @return data
     */
    public List<String> queryTypeId(Integer model, Integer crmType, Long userId);
}
