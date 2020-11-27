package com.kakarote.bi.mapper;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.crm.entity.SimpleCrmEntity;
import com.kakarote.core.utils.BiTimeUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface BiCustomerMapper {

    public List<JSONObject> totalCustomerStats(Map<String, Object> map);

    public List<JSONObject> totalCustomerTable(BiTimeUtil.BiTimeEntity entity);

    public List<JSONObject> customerRecordStats(Map<String, Object> map);

    public List<JSONObject> customerRecordInfo(BiTimeUtil.BiTimeEntity entity);

    public List<JSONObject> customerRecordCategoryStats(BiTimeUtil.BiTimeEntity entity);

    public JSONObject customerConversionStats(Map<String, Object> map);

    public BasePage<JSONObject> customerConversionInfo(BasePage<JSONObject> page, @Param("sqlDateFormat") String sqlDateFormat, @Param("userIds") List<Long> userIds, @Param("time") String time);

    public List<JSONObject> poolStats(Map<String, Object> map);

    public List<JSONObject> poolTable(BiTimeUtil.BiTimeEntity entity);

    public List<JSONObject> employeeCycle(Map<String, Object> map);

    public List<JSONObject> employeeCycleInfo(BiTimeUtil.BiTimeEntity entity);

    public List<JSONObject> districtCycle(BiTimeUtil.BiTimeEntity entity, @Param("districts") List<String> districts);

    public List<JSONObject> productCycle(BiTimeUtil.BiTimeEntity entity, @Param("productList") List<SimpleCrmEntity> productList);

    public JSONObject querySatisfactionOptionList();

    public List<JSONObject> customerSatisfactionTable(Map<String,Object> map);

    public List<JSONObject> productSatisfactionTable(Map<String,Object> map);

    public String queryFirstCustomerCreteTime();
}
