package com.kakarote.crm.common.log;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.kakarote.core.common.log.Content;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.crm.constant.CrmEnum;
import com.kakarote.crm.entity.BO.CrmChangeOwnerUserBO;
import com.kakarote.crm.entity.BO.CrmContractSaveBO;
import com.kakarote.crm.entity.PO.CrmInvoice;
import com.kakarote.crm.service.ICrmInvoiceService;

import java.util.ArrayList;
import java.util.List;

public class CrmInvoiceLog {

    private SysLogUtil sysLogUtil = ApplicationContextHolder.getBean(SysLogUtil.class);

    private ICrmInvoiceService crmInvoiceService = ApplicationContextHolder.getBean(ICrmInvoiceService.class);


    public Content updateInvoice(CrmContractSaveBO crmModel) {
        CrmInvoice crmInvoice = BeanUtil.copyProperties(crmModel.getEntity(), CrmInvoice.class);
        String batchId = StrUtil.isNotEmpty(crmInvoice.getBatchId()) ? crmInvoice.getBatchId() : IdUtil.simpleUUID();
        sysLogUtil.updateRecord(crmModel.getField(), Dict.create().set("batchId", batchId).set("dataTableName", "wk_crm_invoice_data"));
        return sysLogUtil.updateRecord(BeanUtil.beanToMap(crmInvoiceService.getById(crmInvoice.getInvoiceId())), BeanUtil.beanToMap(crmInvoice), CrmEnum.INVOICE, crmInvoice.getInvoiceApplyNumber());
    }

    public Content updateInvoiceStatus(CrmInvoice crmInvoice) {
        CrmInvoice crmInvoice1 = crmInvoiceService.getById(crmInvoice.getInvoiceId());
        String detail = "将发票"+crmInvoice1.getInvoiceApplyNumber()+"标记为已开票。";
        return new Content(crmInvoice1.getInvoiceApplyNumber(),detail);
    }

    public Content resetInvoiceStatus(CrmInvoice crmInvoice) {
        CrmInvoice crmInvoice1 = crmInvoiceService.getById(crmInvoice.getInvoiceId());
        String detail = "将发票"+crmInvoice1.getInvoiceApplyNumber()+"重置开票状态。";
        return new Content(crmInvoice1.getInvoiceApplyNumber(),detail);
    }

    public List<Content> deleteByIds(List<Integer> ids) {
        List<Content> contentList = new ArrayList<>();
        for (Integer id : ids) {
            CrmInvoice crmInvoice1 = crmInvoiceService.getById(id);
            contentList.add(sysLogUtil.addDeleteActionRecord(CrmEnum.INVOICE,crmInvoice1.getInvoiceApplyNumber()));
        }
        return contentList;
    }

    public List<Content> changeOwnerUser(CrmChangeOwnerUserBO crmChangeOwnerUserBO) {
        List<Content> contentList = new ArrayList<>();
        for (Integer id : crmChangeOwnerUserBO.getIds()) {
            CrmInvoice invoice = crmInvoiceService.getById(id);
            contentList.add(sysLogUtil.addConversionRecord(CrmEnum.INVOICE, crmChangeOwnerUserBO.getOwnerUserId(), invoice.getInvoiceApplyNumber()));
        }
        return contentList;
    }


}
