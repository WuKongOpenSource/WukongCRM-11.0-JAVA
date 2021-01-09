package com.kakarote.crm.service;

import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.PageEntity;
import com.kakarote.core.servlet.BaseService;
import com.kakarote.crm.entity.PO.CrmMarketingForm;

import java.util.List;

/**
 * @author JiaS
 * @date 2020/12/2
 */
public interface ICrmMarketingFormService extends BaseService<CrmMarketingForm> {

    CrmMarketingForm saveOrUpdateCrmMarketingForm(CrmMarketingForm crmMarketingForm);

    BasePage<CrmMarketingForm> queryCrmMarketingFormList(PageEntity pageEntity);

    List<CrmMarketingForm> queryAllCrmMarketingFormList();

    void deleteCrmMarketingForm(Integer id);

    void updateStatus(CrmMarketingForm crmMarketingForm);
}
