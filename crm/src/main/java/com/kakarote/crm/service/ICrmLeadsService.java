package com.kakarote.crm.service;

import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.crm.entity.CrmEventBO;
import com.kakarote.core.feign.crm.entity.QueryEventCrmPageBO;
import com.kakarote.core.servlet.BaseService;
import com.kakarote.core.servlet.upload.FileEntity;
import com.kakarote.crm.common.CrmModel;
import com.kakarote.crm.entity.BO.CrmModelSaveBO;
import com.kakarote.crm.entity.BO.CrmSearchBO;
import com.kakarote.crm.entity.BO.CrmUpdateInformationBO;
import com.kakarote.crm.entity.PO.CrmLeads;
import com.kakarote.crm.entity.VO.CrmInfoNumVO;
import com.kakarote.crm.entity.VO.CrmModelFiledVO;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 线索表 服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-21
 */
public interface ICrmLeadsService extends BaseService<CrmLeads> {
    /**
     * 查询字段配置
     *
     * @param id 主键ID
     * @return data
     */
    public List<CrmModelFiledVO> queryField(Integer id);

    /**
     * 分页查询
     *
     * @param search
     * @return
     */
    public BasePage<Map<String, Object>> queryPageList(@RequestBody CrmSearchBO search);

    /**
     * 查询字段配置
     *
     * @param id 主键ID
     * @return data
     */
    public CrmModel queryById(Integer id);

    /**
     * 保存或新增信息
     *
     * @param crmModel model
     * @param isExcel 是否excel
     */
    public void addOrUpdate(CrmModelSaveBO crmModel,boolean isExcel);

    /**
     * 删除线索数据
     *
     * @param ids ids
     */
    public void deleteByIds(List<Integer> ids);

    /**
     * 修改线索负责人
     *
     * @param leadsIds       线索id列表
     * @param newOwnerUserId 新负责人ID
     */
    public void changeOwnerUser(List<Integer> leadsIds, Long newOwnerUserId);

    /**
     * 线索转客户功能
     *
     * @param leadsIds 线索id
     */
    public void transfer(List<Integer> leadsIds);

    /**
     * 下载导入模板
     *
     * @param response 线索id
     * @throws IOException exception
     */
    public void downloadExcel(HttpServletResponse response) throws IOException;

    /**
     * 全部导出
     *
     * @param response resp
     * @param search   搜索对象
     */
    public void exportExcel(HttpServletResponse response, CrmSearchBO search);

    /**
     * 标星
     * @param leads 线索id
     */
    public void star(Integer leads);

    /**
     * 查询详情页基本信息
     * @param leadsId id
     * @return data
     */
    public List<CrmModelFiledVO> information(Integer leadsId);

    /**
     * 查询详情页数量
     * @param leadsId id
     * @return data
     */
    public CrmInfoNumVO num(Integer leadsId);

    /**
     * 查询文件列表
     * @param leadsId id
     * @return file
     */
    public List<FileEntity> queryFileList(Integer leadsId);

    void updateInformation(CrmUpdateInformationBO updateInformationBO);

    List<String> eventLeads(CrmEventBO crmEventBO);

    BasePage<Map<String, Object>> eventLeadsPageList(QueryEventCrmPageBO eventCrmPageBO);
}
