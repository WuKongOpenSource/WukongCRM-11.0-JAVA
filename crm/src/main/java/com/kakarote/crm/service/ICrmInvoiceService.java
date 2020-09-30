package com.kakarote.crm.service;

import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;
import com.kakarote.core.servlet.upload.FileEntity;
import com.kakarote.crm.entity.BO.CrmSearchBO;
import com.kakarote.crm.entity.PO.CrmInvoice;
import com.kakarote.crm.entity.PO.CrmInvoiceInfo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-07-06
 */
public interface ICrmInvoiceService extends BaseService<CrmInvoice> {

    void saveInvoice(CrmInvoice crmInvoice, Long checkUserId);

    void updateInvoice(CrmInvoice crmInvoice, Long checkUserId);

    CrmInvoice queryById(Integer invoiceId);

    void updateInvoiceStatus(CrmInvoice crmInvoice);

    void deleteByIds(List<Integer> ids);

    void changeOwnerUser(List<Integer> ids, Long ownerUserId);

    List<FileEntity> queryFileList(Integer id);

    void saveInvoiceInfo(CrmInvoiceInfo crmInvoiceInfo);

    void updateInvoiceInfo(CrmInvoiceInfo crmInvoiceInfo);

    void deleteInvoiceInfo(Integer infoId);

    BasePage<Map<String, Object>> queryPageList(CrmSearchBO search);
}
