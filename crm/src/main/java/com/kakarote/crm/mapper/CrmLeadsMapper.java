package com.kakarote.crm.mapper;

import com.kakarote.core.feign.crm.entity.CrmEventBO;
import com.kakarote.core.servlet.BaseMapper;
import com.kakarote.crm.common.CrmModel;
import com.kakarote.crm.entity.PO.CrmLeads;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 线索表 Mapper 接口
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-21
 */
public interface CrmLeadsMapper extends BaseMapper<CrmLeads> {
    /**
     * 通过id查询线索数据
     *
     * @param id id
     * @param userId 用户ID
     * @return data
     */
    public CrmModel queryById(@Param("id") Integer id, @Param("userId") Long userId);

    List<String> eventLeads(@Param("data") CrmEventBO crmEventBO);

    List<Integer> eventLeadsList(@Param("userId") Long userId,@Param("time") Date time);
}
