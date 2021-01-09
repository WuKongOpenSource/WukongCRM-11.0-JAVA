package com.kakarote.crm.common.log;

import com.kakarote.core.common.log.BehaviorEnum;
import com.kakarote.core.common.log.Content;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.crm.service.ICrmProductCategoryService;

public class CrmProductCategoryLog {

    private ICrmProductCategoryService crmProductCategoryService = ApplicationContextHolder.getBean(ICrmProductCategoryService.class);

    public Content deleteById(Integer id) {
        String productCategoryName = crmProductCategoryService.getProductCategoryName(id);
        return new Content(productCategoryName,"删除了产品类型:"+productCategoryName, BehaviorEnum.DELETE);
    }
}
