package com.kakarote.crm.mapper;

import com.kakarote.core.servlet.BaseMapper;
import com.kakarote.crm.entity.PO.CrmContractProduct;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 合同产品关系表 Mapper 接口
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-27
 */
public interface CrmContractProductMapper extends BaseMapper<CrmContractProduct> {
    /**
     * 根据合同ID查询合同产品关系表
     * @param contractId 合同ID
     */
    public List<CrmContractProduct> queryByContractId(@Param("contractId") Integer contractId);

    /**
     * 查询合同下产品
     * @param contractId 合同ID
     * @return data
     */
    public List<CrmContractProduct> queryList(@Param("contractId") Integer contractId);
}
