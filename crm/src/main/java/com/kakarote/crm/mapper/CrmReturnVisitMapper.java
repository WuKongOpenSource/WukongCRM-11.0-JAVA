package com.kakarote.crm.mapper;

import com.kakarote.core.servlet.BaseMapper;
import com.kakarote.crm.common.CrmModel;
import com.kakarote.crm.entity.PO.CrmReturnVisit;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-07-06
 */
public interface CrmReturnVisitMapper extends BaseMapper<CrmReturnVisit> {
    /**
     * 通过id查询客户数据
     *
     * @param id     id
     * @return data
     */
    public  CrmModel queryById(@Param("id") Integer id);
}
