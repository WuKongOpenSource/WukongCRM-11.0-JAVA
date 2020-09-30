package com.kakarote.crm.service;

import com.kakarote.core.servlet.BaseService;
import com.kakarote.crm.entity.PO.CrmContactsUserStar;

import java.util.List;

/**
 * <p>
 * 用户联系人标星关系表  服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-26
 */
public interface ICrmContactsUserStarService extends BaseService<CrmContactsUserStar> {
    /**
     * 判断用户是否关注
     * @param contactsId id
     * @param userId 用户ID
     * @return int
     */
    public Integer isStar(Object contactsId, Long userId);

    List<Integer> starList(Long userId);
}
