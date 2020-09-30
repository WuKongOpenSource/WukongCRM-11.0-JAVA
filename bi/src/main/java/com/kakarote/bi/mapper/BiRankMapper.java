package com.kakarote.bi.mapper;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface BiRankMapper {

    public List<JSONObject> addressAnalyse(Map<String,Object> map);

    public List<JSONObject> portrait(Map<String,Object> map);

    public List<JSONObject> portraitLevel(Map<String,Object> map);

    public List<JSONObject> portraitSource(Map<String,Object> map);

    public List<JSONObject> contractProductRanKing(Map<String,Object> map);

    public List<JSONObject> contractRanKing(Map<String,Object> map);

    public List<JSONObject> receivablesRanKing(Map<String,Object> map);

    public List<JSONObject> contractCountRanKing(Map<String,Object> map);

    public List<JSONObject> productCountRanKing(Map<String,Object> map);

    public List<JSONObject> customerCountRanKing(Map<String,Object> map);

    public List<JSONObject> contactsCountRanKing(Map<String,Object> map);

    public List<JSONObject> customerGenjinCountRanKing(Map<String,Object> map);

    public List<JSONObject> recordCountRanKing(Map<String,Object> map);

    public List<JSONObject> travelCountRanKing(Map<String,Object> map);

    List<JSONObject> contractRanKing1(@Param("data") Map<String, Object> toMap, @Param("monthMap") List<Map<String, String>> monthMap);

    List<JSONObject> receivablesRanKing1(@Param("data") Map<String, Object> toMap,@Param("monthMap") List<Map<String, String>> monthMap);
}
