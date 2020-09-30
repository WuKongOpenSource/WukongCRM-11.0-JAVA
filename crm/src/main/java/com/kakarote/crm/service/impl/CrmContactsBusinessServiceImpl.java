package com.kakarote.crm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.crm.entity.PO.CrmContactsBusiness;
import com.kakarote.crm.mapper.CrmContactsBusinessMapper;
import com.kakarote.crm.service.ICrmContactsBusinessService;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * <p>
 * 商机联系人关联表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-26
 */
@Service
public class CrmContactsBusinessServiceImpl extends BaseServiceImpl<CrmContactsBusinessMapper, CrmContactsBusiness> implements ICrmContactsBusinessService {

    /**
     * 保存
     *
     * @param business   商机ID
     * @param contactsId 联系人ID
     */
    @Override
    public void save(Integer business, Integer contactsId) {
        lambdaUpdate().eq(CrmContactsBusiness::getBusinessId, business).eq(CrmContactsBusiness::getContactsId, contactsId).remove();
//        if (count == 0){
            CrmContactsBusiness contactsBusiness = new CrmContactsBusiness();
            contactsBusiness.setBusinessId(business);
            contactsBusiness.setContactsId(contactsId);
            save(contactsBusiness);
//        }

    }

    /**
     * 根据联系人ID删除联系人商机关联
     *
     * @param contactsId 联系人ID
     */
    @Override
    public void removeByContactsId(Integer contactsId) {
        LambdaQueryWrapper<CrmContactsBusiness> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CrmContactsBusiness::getContactsId, contactsId);
        remove(wrapper);
    }

    /**
     * 根据商机ID删除联系人商机关联
     *
     * @param businessId 商机ID
     */
    @Override
    public void removeByBusinessId(Integer... businessId) {
        LambdaQueryWrapper<CrmContactsBusiness> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(CrmContactsBusiness::getBusinessId, Arrays.asList(businessId));
        remove(wrapper);
    }
}
