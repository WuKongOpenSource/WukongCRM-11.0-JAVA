package com.kakarote.crm.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.kakarote.core.feign.crm.entity.CustomerStats;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author JiaS
 * @date 2020/9/16
 */
@Repository
public interface CustomerStatsMapper{

    @SqlParser(filter = true)
    List<CustomerStats> selectCustomerStats(@Param("startId") Long startId,@Param("endId") Long endId);

    @SqlParser(filter = true)
    Long maxCustomerId();

    @SqlParser(filter = true)
    Long countCustomerStats(@Param("tableName") String tableName);

    @SqlParser(filter = true)
    Integer createTableForCustomerStats(@Param("tableName") String tableName);

    @SqlParser(filter = true)
    Integer saveBatchByCustomerStats(@Param("map") Map<String,Object> map);

}
