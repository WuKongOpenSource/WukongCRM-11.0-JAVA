package com.kakarote.crm.service;

import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;
import com.kakarote.crm.entity.BO.CrmPrintTemplateBO;
import com.kakarote.crm.entity.PO.CrmPrintRecord;
import com.kakarote.crm.entity.PO.CrmPrintTemplate;
import com.kakarote.crm.entity.VO.CrmPrintFieldVO;

import java.util.List;

/**
 * <p>
 * 打印模板表 服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-04-27
 */
public interface ICrmPrintTemplateService extends BaseService<CrmPrintTemplate> {

    /**
     * 分页查询打印模板列表
     * @param templateBO search
     * @return data
     */
    public BasePage<CrmPrintTemplate> queryPrintTemplateList(CrmPrintTemplateBO templateBO);

    /**
     * 删除打印模板
     * @param templateId templateId
     */
    public void deletePrintTemplate(Integer templateId);

    /**
     * 查询字段列表
     * @param crmType type
     * @return data
     */
    public CrmPrintFieldVO queryFields(Integer crmType);

    /**
     * 打印
     * @param templateId templateId
     * @param id id
     * @return data
     */
    public String print(Integer templateId, Integer id);

    /**
     * 预览
     *
     * @param content content
     * @param type    type
     * @return path
     */
    public String preview(String content, String type);

    /**
     * 复制模板
     * @param templateId templateId
     */
    public void copyTemplate(Integer templateId);

    /**
     * 保存打印记录
     * @param crmPrintRecord record
     */
    public void savePrintRecord(CrmPrintRecord crmPrintRecord);

    /**
     * 查询打印记录
     * @param crmType crm类型
     * @param typeId 类型ID
     */
    public List<CrmPrintRecord> queryPrintRecord(Integer crmType, Integer typeId);

    /**
     * 根据ID查询
     * @param recordId recordId
     * @return data
     */
    public CrmPrintRecord queryPrintRecordById(Integer recordId);
}
