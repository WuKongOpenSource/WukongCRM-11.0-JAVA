package com.kakarote.crm.service;

import com.kakarote.core.servlet.BaseService;
import com.kakarote.crm.entity.PO.CrmBusinessUserStar;

import java.util.List;

/**
 * <p>
 * 用户商机标星关系表  服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-27
 */
public interface ICrmBusinessUserStarService extends BaseService<CrmBusinessUserStar> {
    /**
     * 判断用户是否关注
     * @param businessId id
     * @param userId 用户ID
     * @return int
     */
    public Integer isStar(Object businessId, Long userId);

    List<Integer> starList(Long userId);
}
