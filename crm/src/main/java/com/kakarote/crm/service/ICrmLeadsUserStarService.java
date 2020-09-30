package com.kakarote.crm.service;

import com.kakarote.core.servlet.BaseService;
import com.kakarote.crm.entity.PO.CrmLeadsUserStar;

import java.util.List;

/**
 * <p>
 * 用户线索标星关系表  服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-21
 */
public interface ICrmLeadsUserStarService extends BaseService<CrmLeadsUserStar> {
    /**
     * 判断用户是否关注
     * @param leadsId id
     * @param userId 用户ID
     * @return int
     */
    public Integer isStar(Object leadsId, Long userId);

    List<Integer> starList(Long userId);
}
