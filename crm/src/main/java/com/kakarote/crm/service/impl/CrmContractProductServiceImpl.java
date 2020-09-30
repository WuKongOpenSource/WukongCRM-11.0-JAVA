package com.kakarote.crm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.crm.entity.PO.CrmContractProduct;
import com.kakarote.crm.mapper.CrmContractProductMapper;
import com.kakarote.crm.service.ICrmContractProductService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 合同产品关系表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-27
 */
@Service
public class CrmContractProductServiceImpl extends BaseServiceImpl<CrmContractProductMapper, CrmContractProduct> implements ICrmContractProductService {

    /**
     * 根据合同ID删除合同产品关系表
     *
     * @param contractId 合同ID
     */
    @Override
    public void deleteByContractId(Integer... contractId) {
        LambdaQueryWrapper<CrmContractProduct> wrapper=new LambdaQueryWrapper<>();
        wrapper.in(CrmContractProduct::getContractId, Arrays.asList(contractId));
        remove(wrapper);
    }

    /**
     * 根据合同ID查询合同产品关系表
     * @param contractId 合同ID
     */
    @Override
    public List<CrmContractProduct> queryByContractId(Integer contractId) {
        return getBaseMapper().queryByContractId(contractId);
    }

    /**
     * 查询商机下产品
     *
     * @param contractId 合同ID
     * @return data
     */
    @Override
    public List<CrmContractProduct> queryList(Integer contractId) {
        if (contractId == null) {
            return new ArrayList<>();
        }
        return getBaseMapper().queryList(contractId);
    }
}
