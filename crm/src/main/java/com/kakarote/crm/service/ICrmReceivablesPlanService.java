package com.kakarote.crm.service;

import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;
import com.kakarote.core.servlet.upload.FileEntity;
import com.kakarote.crm.common.CrmModel;
import com.kakarote.crm.constant.CrmEnum;
import com.kakarote.crm.entity.BO.CrmBusinessSaveBO;
import com.kakarote.crm.entity.BO.CrmReceivablesPlanBO;
import com.kakarote.crm.entity.BO.CrmSearchBO;
import com.kakarote.crm.entity.BO.CrmUpdateInformationBO;
import com.kakarote.crm.entity.PO.CrmReceivablesPlan;
import com.kakarote.crm.entity.VO.CrmModelFiledVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 回款计划表 服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-28
 */
public interface ICrmReceivablesPlanService extends BaseService<CrmReceivablesPlan> {


    /**
     * 查询所有数据
     *
     * @param search 搜索对象
     * @return data
     */
    public BasePage<Map<String, Object>> queryPageList(CrmSearchBO search);

    /**
     * 查询字段配置
     *
     * @param id 主键ID
     * @return data
     */
    public CrmModel queryById(Integer id);

    /**
     * 批量保存回款计划
     * @param receivablesPlans 回款计划列表
     */
    public void batchSave(List<CrmReceivablesPlanBO> receivablesPlans);


    /**
     * 根据合同ID删除回款计划
     * @param contractId 合同ID
     */
    public void deleteByContractId(Integer contractId);

    /**
     * 修改回款计划状态
     * @param crmEnum crmEnum
     * @param object 对应的PO对象
     * @param examineStatus 审批状态
     */
    public void updateReceivedStatus(CrmEnum crmEnum, Object object, Integer examineStatus);


    /**
     * 定时回款计划状态
     */
    public void updateReceivedStatus();

    /**
     * 全部导出
     *
     * @param response resp
     * @param search   搜索对象
     */
    public void exportExcel(HttpServletResponse response, CrmSearchBO search);

    /**
     * 保存或修改
     * @param crmModel data
     */
    public void addOrUpdate(CrmBusinessSaveBO crmModel);

    /**
     * 查询详情页基本信息
     * @param receivablesPlanId id
     * @return data
     */
    public List<CrmModelFiledVO> information(Integer receivablesPlanId);

    /**
     * 删除ids
     * @param ids ids
     */
    public void deleteByIds(List<Integer> ids);

    /**
     * 修改基本信息
     * @param updateInformationBO data
     */
    public void updateInformation(CrmUpdateInformationBO updateInformationBO);

    /**
     * 查询新增所需字段
     * @param id id
     */
    public List<CrmModelFiledVO> queryField(Integer id);

    /**
     * 查询排序后字段
     * @param id id
     * @return list
     */
    public  List<List<CrmModelFiledVO>> queryFormPositionField(Integer id);

    /**
     * 根据客户ID查询未被使用回款计划
     * @param crmReceivablesPlanBO param
     * @return data
     */
    public List<CrmReceivablesPlan> queryByContractAndCustomer(CrmReceivablesPlanBO crmReceivablesPlanBO);

    String getReceivablesPlanNum(Integer receivablesPlanId);

    /**
     * 查询文件列表
     * @param receivablesPlanId id
     * @return file
     */
    public List<FileEntity> queryFileList(Integer receivablesPlanId);
}
