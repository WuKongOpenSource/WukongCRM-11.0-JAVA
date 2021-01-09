package com.kakarote.crm.common.log;

import com.kakarote.core.common.log.BehaviorEnum;
import com.kakarote.core.common.log.Content;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.crm.entity.PO.CrmCustomerPool;
import com.kakarote.crm.service.ICrmCustomerPoolService;

public class CrmCustomerPoolLog {

    private ICrmCustomerPoolService crmCustomerPoolService = ApplicationContextHolder.getBean(ICrmCustomerPoolService.class);

    public Content changeStatus(Integer poolId,Integer status) {
        CrmCustomerPool customerPool = crmCustomerPoolService.getById(poolId);
        String detail;
        if (status == 0){
            detail = "停用了:";
        }else {
            detail = "启用了:";
        }
        return new Content(customerPool.getPoolName(),detail+customerPool.getPoolName(), BehaviorEnum.UPDATE);
    }

    public Content transfer( Integer prePoolId,  Integer postPoolId) {
        CrmCustomerPool prePool = crmCustomerPoolService.getById(prePoolId);
        CrmCustomerPool postPool = crmCustomerPoolService.getById(postPoolId);
        return new Content("公海转移","从["+prePool.getPoolName()+"]转移到公海["+postPool.getPoolName()+"]", BehaviorEnum.UPDATE);
    }

    public Content deleteCustomerPool(Integer poolId) {
        CrmCustomerPool customerPool = crmCustomerPoolService.getById(poolId);
        return new Content(customerPool.getPoolName(),"删除了公海"+customerPool.getPoolName(), BehaviorEnum.UPDATE);
    }
}
