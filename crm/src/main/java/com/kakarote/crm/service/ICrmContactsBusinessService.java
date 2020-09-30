package com.kakarote.crm.service;

import com.kakarote.core.servlet.BaseService;
import com.kakarote.crm.entity.PO.CrmContactsBusiness;

/**
 * <p>
 * 商机联系人关联表 服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-26
 */
public interface ICrmContactsBusinessService extends BaseService<CrmContactsBusiness> {
    /**
     * 保存
     * @param business 商机ID
     * @param contactsId 联系人ID
     */
    public void save(Integer business,Integer contactsId);

    /**
     * 根据联系人ID删除联系人商机关联
     * @param contactsId 联系人ID
     */
    public void removeByContactsId(Integer contactsId);

    /**
     * 根据商机ID删除联系人商机关联
     * @param businessId 商机ID
     */
    public void removeByBusinessId(Integer... businessId);
}
