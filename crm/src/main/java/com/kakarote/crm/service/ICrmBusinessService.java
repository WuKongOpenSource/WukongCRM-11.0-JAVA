package com.kakarote.crm.service;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.crm.entity.CrmEventBO;
import com.kakarote.core.feign.crm.entity.QueryEventCrmPageBO;
import com.kakarote.core.feign.crm.entity.SimpleCrmEntity;
import com.kakarote.core.servlet.BaseService;
import com.kakarote.core.servlet.upload.FileEntity;
import com.kakarote.crm.common.CrmModel;
import com.kakarote.crm.entity.BO.*;
import com.kakarote.crm.entity.PO.CrmBusiness;
import com.kakarote.crm.entity.PO.CrmContacts;
import com.kakarote.crm.entity.VO.CrmInfoNumVO;
import com.kakarote.crm.entity.VO.CrmMembersSelectVO;
import com.kakarote.crm.entity.VO.CrmModelFiledVO;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商机表 服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-27
 */
public interface ICrmBusinessService extends BaseService<CrmBusiness> {

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
     */
    public void addOrUpdate(CrmBusinessSaveBO crmModel);

    /**
     * 删除商机数据
     *
     * @param ids ids
     */
    public void deleteByIds(List<Integer> ids);

    /**
     * 修改商机负责人
     *
     * @param changOwnerUserBO       data
     */
    public void changeOwnerUser(CrmBusinessChangOwnerUserBO changOwnerUserBO);

    /**
     * 全部导出
     *
     * @param response resp
     * @param search   搜索对象
     */
    public void exportExcel(HttpServletResponse response, CrmSearchBO search);

    /**
     * 查询商机下联系人
     * @param pageEntity entity
     * @return data
     */
    public BasePage<CrmContacts> queryContacts(CrmContactsPageBO pageEntity);

    /**
     * 查询详情信息
     * @param businessId 商机id
     * @return data
     */
    public List<CrmModelFiledVO> information(Integer businessId);

    /**
     * 标星
     * @param businessId 商机id
     */
    public void star(Integer businessId);

    /**
     * 查询文件数量
     *
     * @param businessId id
     * @return data
     */
    public CrmInfoNumVO num(Integer businessId);

    /**
     * 查询文件列表
     * @param businessId id
     * @return file
     */
    public List<FileEntity> queryFileList(Integer businessId);

    /**
     * 设置首要联系人
     * @param contactsBO  data
     */
    public void setContacts(CrmFirstContactsBO contactsBO);

    /**
     * 获取团队成员
     * @param businessId 商机ID
     * @return data
     */
    public List<CrmMembersSelectVO> getMembers(Integer businessId);

    /**
     * 添加团队成员
     * @param crmMemberSaveBO data
     */
    public void addMember(CrmMemberSaveBO crmMemberSaveBO);

    /**
     * 删除团队成员
     * @param crmMemberSaveBO data
     */
    public void deleteMember(CrmMemberSaveBO crmMemberSaveBO);

    /**
     * 退出团队
     * @param businessId 商机ID
     */
    public void exitTeam(Integer businessId);

    /**
     * 商机关联联系人
     * @param relevanceBusinessBO 业务对象
     */
    public void relateContacts(CrmRelevanceBusinessBO relevanceBusinessBO);

    /**
     * 商机解除+关联联系人
     * @param relevanceBusinessBO 业务对象
     */
    public void unrelateContacts(CrmRelevanceBusinessBO relevanceBusinessBO);

    public List<SimpleCrmEntity> querySimpleEntity(List<Integer> ids);

    String getBusinessName(int businessId);

    void updateInformation(CrmUpdateInformationBO updateInformationBO);

    JSONObject queryProduct(CrmBusinessQueryRelationBO businessQueryProductBO);

    BasePage<JSONObject> queryContract(CrmBusinessQueryRelationBO businessQueryRelationBO);

    List<String> eventDealBusiness(CrmEventBO crmEventBO);

    BasePage<Map<String, Object>> eventDealBusinessPageList(QueryEventCrmPageBO eventCrmPageBO);

    List<String> eventBusiness(CrmEventBO crmEventBO);

    BasePage<Map<String, Object>> eventBusinessPageList(QueryEventCrmPageBO eventCrmPageBO);
}
