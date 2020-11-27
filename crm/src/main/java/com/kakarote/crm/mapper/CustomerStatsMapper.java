package com.kakarote.crm.mapper;

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
public interface CustomerStatsMapper {

    public List<CustomerStats> selectCustomerStats(@Param("startId") Integer startId);

    public Integer queryLastCustomerId(@Param("startId") Integer startId);

    public Integer queryStartCustomerId();

    public Integer createTableForCustomerStats(@Param("tableName") String tableName);

    public void saveStatsInfo(@Param("lastCustomerId") Integer lastCustomerId, @Param("num") Integer num);

    public void saveBatchByCustomerStats(@Param("map") Map<String, Object> map);

}