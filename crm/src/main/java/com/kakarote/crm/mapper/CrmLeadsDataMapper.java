package com.kakarote.crm.mapper;

import com.kakarote.core.servlet.BaseMapper;
import com.kakarote.crm.entity.PO.CrmLeadsData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 线索自定义字段存值表 Mapper 接口
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-21
 */
public interface CrmLeadsDataMapper extends BaseMapper<CrmLeadsData> {
    /**
     * 通过batchId查询自定义字段数据
     * @param batchId batchId
     * @return data
     */
    List<CrmLeadsData> queryDataByBatchId(@Param("batchId") String batchId);
}
