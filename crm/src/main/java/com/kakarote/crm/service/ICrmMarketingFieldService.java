package com.kakarote.crm.service;

import com.kakarote.core.servlet.BaseService;
import com.kakarote.crm.entity.BO.MarketingFieldBO;
import com.kakarote.crm.entity.PO.CrmMarketingField;

import java.util.List;

/**
 * @author JiaS
 * @date 2020/12/2
 */
public interface ICrmMarketingFieldService extends BaseService<CrmMarketingField> {

    List<CrmMarketingField> queryField(Integer id);

    void recordToFormType(List<CrmMarketingField> list);

    void transferFieldList(List<CrmMarketingField> recordList, Integer isDetail);

    void saveField(MarketingFieldBO marketingFieldBO);
}
