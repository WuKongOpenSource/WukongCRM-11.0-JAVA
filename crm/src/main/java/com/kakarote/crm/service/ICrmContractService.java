package com.kakarote.crm.service;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.crm.entity.BiParams;
import com.kakarote.core.feign.crm.entity.CrmEventBO;
import com.kakarote.core.feign.crm.entity.QueryEventCrmPageBO;
import com.kakarote.core.feign.crm.entity.SimpleCrmEntity;
import com.kakarote.core.servlet.BaseService;
import com.kakarote.core.servlet.upload.FileEntity;
import com.kakarote.crm.common.CrmModel;
import com.kakarote.crm.entity.BO.*;
import com.kakarote.crm.entity.PO.CrmContract;
import com.kakarote.crm.entity.PO.CrmReceivablesPlan;
import com.kakarote.crm.entity.VO.CrmInfoNumVO;
import com.kakarote.crm.entity.VO.CrmMembersSelectVO;
import com.kakarote.crm.entity.VO.CrmModelFiledVO;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 合同表 服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-27
 */
public interface ICrmContractService extends BaseService<CrmContract> {

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
    public void addOrUpdate(CrmContractSaveBO crmModel);

    /**
     * 删除合同数据
     *
     * @param ids ids
     */
    public void deleteByIds(List<Integer> ids);

    /**
     * 修改合同负责人
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
     * 获取团队成员
     * @param contractId 合同ID
     * @return data
     */
    public List<CrmMembersSelectVO> getMembers(Integer contractId);

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
     * @param contractId 合同ID
     */
    public void exitTeam(Integer contractId);

    /**
     * 查询详情信息
     * @param contractId 合同ID
     * @return data
     */
    public List<CrmModelFiledVO> information(Integer contractId);

    /**
     * 查询产品通过合同ID
     * @param crmRelationPageBO 合同ID
     * @return data
     */
    public JSONObject queryProductListByContractId(CrmRelationPageBO crmRelationPageBO);

    /**
     * 查询文件数量
     *
     * @param contractId id
     * @return data
     */
    public CrmInfoNumVO num(Integer contractId);

    /**
     * 查询文件列表
     * @param contractId id
     * @return file
     */
    public List<FileEntity> queryFileList(Integer contractId);

    /**
     * 查询回款计划通过合同ID
     * @param crmRelationPageBO 合同ID
     * @return data
     */
    public BasePage<CrmReceivablesPlan> queryReceivablesPlanListByContractId(CrmRelationPageBO crmRelationPageBO);

    public List<SimpleCrmEntity> querySimpleEntity(List<Integer> ids);

    String getContractName(int contractId);

    void updateInformation(CrmUpdateInformationBO updateInformationBO);

    BasePage<JSONObject> queryListByContractId(CrmRelationPageBO crmRelationPageBO);

    List<CrmReceivablesPlan> queryReceivablesPlansByContractId(Integer contractId, Integer receivablesId);

    BasePage<JSONObject> queryReturnVisit(CrmRelationPageBO crmRelationPageBO);

    void contractDiscard(Integer contractId);

    List<String> endContract(CrmEventBO crmEventBO);

    List<String> receiveContract(CrmEventBO crmEventBO);

    BasePage<Map<String,Object>> eventContractPageList(QueryEventCrmPageBO eventCrmPageBO);

    /**
     * 根据产品ID查询合同
     * @param biParams 参数
     * @return data
     */
    public BasePage<Map<String,Object>> queryListByProductId(BiParams biParams);
}
