package com.kakarote.crm.service;

import com.kakarote.core.servlet.BaseService;
import com.kakarote.crm.entity.PO.CrmCustomerUserStar;

import java.util.List;

/**
 * <p>
 * 用户客户标星关系表  服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-29
 */
public interface ICrmCustomerUserStarService extends BaseService<CrmCustomerUserStar> {
    /**
     * 判断用户是否关注
     * @param customerId id
     * @param userId 用户ID
     * @return int
     */
    public Integer isStar(Object customerId, Long userId);

    List<Integer> starList(Long userId);
}
