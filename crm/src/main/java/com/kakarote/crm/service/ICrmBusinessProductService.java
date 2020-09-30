package com.kakarote.crm.service;

import com.kakarote.core.servlet.BaseService;
import com.kakarote.crm.entity.PO.CrmBusinessProduct;

import java.util.List;

/**
 * <p>
 * 商机产品关系表 服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-27
 */
public interface ICrmBusinessProductService extends BaseService<CrmBusinessProduct> {
    /**
     * 通过商机ID删除商机产品关联
     * @param ids ids
     */
    public void deleteByBusinessId(Integer... ids);

    /**
     * 保存商机产品关联
     * @param crmBusinessProductList data
     */
    public void save(List<CrmBusinessProduct> crmBusinessProductList);

    /**
     * 通过产品ID删除商机产品关联
     * @param ids ids
     */
    public void deleteByProductId(Integer... ids);

    /**
     * 查询商机下产品
     * @param businessId 商机ID
     * @return data
     */
    public List<CrmBusinessProduct> queryList(Integer businessId);
}
