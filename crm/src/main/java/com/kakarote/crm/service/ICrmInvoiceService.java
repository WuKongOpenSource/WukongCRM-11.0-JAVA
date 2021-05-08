package com.kakarote.crm.service;

import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.crm.entity.SimpleCrmEntity;
import com.kakarote.core.servlet.BaseService;
import com.kakarote.core.servlet.upload.FileEntity;
import com.kakarote.crm.entity.BO.CrmContractSaveBO;
import com.kakarote.crm.entity.BO.CrmSearchBO;
import com.kakarote.crm.entity.BO.CrmUpdateInformationBO;
import com.kakarote.crm.entity.PO.CrmInvoice;
import com.kakarote.crm.entity.PO.CrmInvoiceInfo;
import com.kakarote.crm.entity.VO.CrmModelFiledVO;

import javax.servlet.http.HttpServletResponse;
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

    List<SimpleCrmEntity> querySimpleEntity(List<Integer> ids);

    CrmInvoice queryById(Integer invoiceId);

    void updateInvoiceStatus(CrmInvoice crmInvoice);

    void deleteByIds(List<Integer> ids);

    void changeOwnerUser(List<Integer> ids, Long ownerUserId);

    List<FileEntity> queryFileList(Integer id);

    void saveInvoiceInfo(CrmInvoiceInfo crmInvoiceInfo);

    void updateInvoiceInfo(CrmInvoiceInfo crmInvoiceInfo);

    void deleteInvoiceInfo(Integer infoId);

    BasePage<Map<String, Object>> queryPageList(CrmSearchBO search);

    List<CrmModelFiledVO> queryField(Integer id);

    List<List<CrmModelFiledVO>> queryFormPositionField(Integer id);

    void addOrUpdate(CrmContractSaveBO crmModel, boolean isExcel);

    /**
     * 全部导出
     *
     * @param response resp
     * @param search   搜索对象
     */
    void exportExcel(HttpServletResponse response, CrmSearchBO search);

    List<CrmModelFiledVO> information(Integer invoiceId);

    void updateInformation(CrmUpdateInformationBO updateInformationBO);
}
