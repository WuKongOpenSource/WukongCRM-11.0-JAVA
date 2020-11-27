package com.kakarote.crm.service;

import com.alibaba.fastjson.JSONObject;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.PageEntity;
import com.kakarote.core.feign.admin.entity.AdminMessageEnum;
import com.kakarote.core.feign.crm.entity.CrmEventBO;
import com.kakarote.core.feign.crm.entity.QueryEventCrmPageBO;
import com.kakarote.core.feign.crm.entity.SimpleCrmEntity;
import com.kakarote.core.servlet.BaseService;
import com.kakarote.core.servlet.upload.FileEntity;
import com.kakarote.crm.common.CrmModel;
import com.kakarote.crm.entity.BO.*;
import com.kakarote.crm.entity.PO.CrmContacts;
import com.kakarote.crm.entity.PO.CrmCustomer;
import com.kakarote.crm.entity.PO.CrmCustomerSetting;
import com.kakarote.crm.entity.VO.CrmDataCheckVO;
import com.kakarote.crm.entity.VO.CrmInfoNumVO;
import com.kakarote.crm.entity.VO.CrmMembersSelectVO;
import com.kakarote.crm.entity.VO.CrmModelFiledVO;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 客户表 服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-29
 */
public interface ICrmCustomerService extends BaseService<CrmCustomer> {
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
    public CrmModel queryById(Integer id,Integer poolId);

    /**
     * 保存或新增信息
     *
     * @param crmModel model
     */
    public Map<String,Object> addOrUpdate(CrmModelSaveBO crmModel,boolean isExcel,Integer poolId);

    /**
     * 删除客户数据
     *
     * @param ids ids
     */
    public void deleteByIds(List<Integer> ids);


    /**
     * 检测数据有无关联
     *
     * @param ids ids
     */
    JSONObject detectionDataCanBeDelete(List<Integer> ids);

    /**
     * 修改客户负责人
     *
     * @param changOwnerUserBO data
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
     * 客户放入公海
     * @param poolBO bo
     */
    public void updateCustomerByIds(CrmCustomerPoolBO poolBO);

    /**
     * 标星
     *
     * @param customerId 客户id
     */
    public void star(Integer customerId);

    /**
     * 设置首要联系人
     *
     * @param contactsBO data
     */
    public void setContacts(CrmFirstContactsBO contactsBO);

    /**
     * 获取团队成员
     *
     * @param customerId 客户ID
     * @return data
     */
    public List<CrmMembersSelectVO> getMembers(Integer customerId);

    /**
     * 领取或分配客户
     *
     * @param poolBO    bo
     * @param isReceive 领取还是分配
     */
    public void getCustomersByIds(CrmCustomerPoolBO poolBO, Integer isReceive);

    /**
     * 添加团队成员
     *
     * @param crmMemberSaveBO data
     */
    public void addMember(CrmMemberSaveBO crmMemberSaveBO);

    /**
     * 删除团队成员
     *
     * @param crmMemberSaveBO data
     */
    public void deleteMember(CrmMemberSaveBO crmMemberSaveBO);

    /**
     * 退出团队
     *
     * @param customerId 客户ID
     */
    public void exitTeam(Integer customerId);

    /**
     * 下载导入模板
     *
     * @param response resp
     * @throws IOException ex
     */
    public void downloadExcel(HttpServletResponse response) throws IOException;

    /**
     * 保存客户规则设置
     * @param customerSetting setting
     */
    public void customerSetting(CrmCustomerSetting customerSetting);

    /**
     * 删除客户规则设置
     * @param settingId settingId
     */
    public void deleteCustomerSetting(Integer settingId);

    /**
     * 查询详情页基本信息
     *
     * @param customerId id
     * @param poolId     公海ID
     * @return data
     */
    public List<CrmModelFiledVO> information(Integer customerId, Integer poolId);

    /**
     * 修改客户成交状态
     *
     * @param dealStatus 状态
     * @param ids        ids
     */
    public void setDealStatus(Integer dealStatus, List<Integer> ids);

    /**
     * 查询客户规则设置
     * @param pageEntity entity
     * @param type type
     */
    public BasePage<CrmCustomerSetting> queryCustomerSetting(PageEntity pageEntity, Integer type);

    /**
     * 根据客户ID查询联系人
     *
     * @param pageEntity entity
     * @return data
     */
    public BasePage<CrmContacts> queryContacts(CrmContactsPageBO pageEntity);

    /**
     * 根据客户ID查询商机
     *
     * @param pageEntity entity
     * @return data
     */
    public BasePage<Map<String, Object>> queryBusiness(CrmContactsPageBO pageEntity);

    /**
     * 根据客户ID查询合同
     *
     * @param pageEntity entity
     * @return data
     */
    public BasePage<Map<String, Object>> queryContract(CrmContactsPageBO pageEntity);

    /**
     * 锁定或者解锁
     *
     * @param status 状态
     * @param ids    ids
     */
    public void lock(Integer status, List<String> ids);

    public List<SimpleCrmEntity> querySimpleEntity(List<Integer> ids);

    List<SimpleCrmEntity> queryByNameCustomerInfo(String name);

    List<SimpleCrmEntity> queryNameCustomerInfo(String name);

    /**
     * 跟进客户名称查询客户
     *
     * @param name name
     * @return data
     */
    @Cached(expire = 3600, cacheType = CacheType.REMOTE)
    public SimpleCrmEntity queryFirstCustomerByName(String name);

    /**
     * 查询文件数量
     *
     * @param customerId id
     * @return data
     */
    public CrmInfoNumVO num(Integer customerId);

    /**
     * 查询文件列表
     *
     * @param customerId id
     * @return file
     */
    public List<FileEntity> queryFileList(Integer customerId);

    /**
     * 获取客户名称
     *
     * @param customerId id
     * @return data
     */
    public String getCustomerName(Integer customerId);

    /**
     * 团队成员相关消息
     */
    public void addTermMessage(AdminMessageEnum messageEnum, Integer typeId, String title, Long userId);

    boolean isMaxOwner(Long ownerUserId, List<Integer> ids);

    void updateInformation(CrmUpdateInformationBO updateInformationBO);

    List<CrmDataCheckVO> dataCheck(CrmDataCheckBO dataCheckBO);

    BasePage<JSONObject> queryReceivablesPlan(CrmRelationPageBO crmRelationPageBO);

    BasePage<JSONObject> queryReceivables(CrmRelationPageBO crmRelationPageBO);

    BasePage<JSONObject> queryReturnVisit(CrmRelationPageBO crmRelationPageBO);

    BasePage<JSONObject> queryInvoice(CrmRelationPageBO crmRelationPageBO);

    BasePage<JSONObject> queryInvoiceInfo(CrmRelationPageBO crmRelationPageBO);

    BasePage<JSONObject> queryCallRecord(CrmRelationPageBO crmRelationPageBO);

    List<JSONObject> nearbyCustomer(String lng, String lat, Integer type, Integer radius, Long ownerUserId);

    List<String> eventCustomer(CrmEventBO crmEventBO);

    BasePage<Map<String,Object>> eventCustomerPageList(QueryEventCrmPageBO eventCrmPageBO);

    List<Integer> forgottenCustomer(Integer day, List<Long> userIds, String search);

    List<Integer> unContactCustomer(String search, List<Long> userIds);
}
