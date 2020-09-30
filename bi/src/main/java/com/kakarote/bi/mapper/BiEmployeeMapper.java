package com.kakarote.bi.mapper;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.utils.BiTimeUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface BiEmployeeMapper {

    public List<JSONObject> contractNum(Map<String, Object> map);

    public List<JSONObject> contractMoney(Map<String, Object> map);

    public List<JSONObject> receivables(Map<String, Object> map);

    public List<JSONObject> totalContractTable(Map<String, Object> map);

    public JSONObject totalContract(BiTimeUtil.BiTimeEntity entity);

    public JSONObject totalInvoice(@Param("year") Integer year, @Param("userIds") List<Long> userIds);

    public List<JSONObject> invoiceStatsTable(Map<String, Object> map);
}
