package com.kakarote.crm.mapper;

import com.kakarote.core.servlet.BaseMapper;
import com.kakarote.crm.entity.PO.CrmBusinessProduct;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 商机产品关系表 Mapper 接口
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-27
 */
public interface CrmBusinessProductMapper extends BaseMapper<CrmBusinessProduct> {
    /**
     * 查询商机下产品
     * @param businessId 商机ID
     * @return data
     */
    public List<CrmBusinessProduct> queryList(@Param("businessId") Integer businessId);
}
