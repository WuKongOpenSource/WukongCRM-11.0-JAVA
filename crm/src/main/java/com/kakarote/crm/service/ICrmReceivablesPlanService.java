package com.kakarote.crm.service;

import com.kakarote.core.servlet.BaseService;
import com.kakarote.crm.entity.BO.CrmBusinessSaveBO;
import com.kakarote.crm.entity.BO.CrmReceivablesPlanBO;
import com.kakarote.crm.entity.PO.CrmReceivablesPlan;
import com.kakarote.crm.entity.VO.CrmModelFiledVO;

import java.util.List;

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
     * 保存或修改
     * @param crmModel data
     */
    public void saveAndUpdate(CrmBusinessSaveBO crmModel);

    /**
     * 删除ids
     * @param ids ids
     */
    public void deleteByIds(List<Integer> ids);

    /**
     * 查询新增所需字段
     * @param id id
     */
    public List<CrmModelFiledVO> queryField(Integer id);

    /**
     * 根据客户ID查询未被使用回款计划
     * @param crmReceivablesPlanBO param
     * @return data
     */
    public List<CrmReceivablesPlan> queryByContractAndCustomer(CrmReceivablesPlanBO crmReceivablesPlanBO);
    /**
     * 根据合同查询回款计划
     */
    public void qureyListByContractId(CrmReceivablesPlanBO crmReceivablesPlanBO);

    public void queryReceivablesPlansByContractId(CrmReceivablesPlanBO crmReceivablesPlanBO);

    String getReceivablesPlanNum(int planId);
}
