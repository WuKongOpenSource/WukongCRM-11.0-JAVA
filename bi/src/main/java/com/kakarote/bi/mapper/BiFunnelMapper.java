package com.kakarote.bi.mapper;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.entity.BasePage;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface BiFunnelMapper {

    public List<JSONObject> sellFunnel(Map<String,Object> map);

    public List<JSONObject> addBusinessAnalyze(Map<String,Object> map);

    public List<JSONObject> win(Map<String,Object> map);

    public BasePage<JSONObject> sellFunnelList(BasePage<JSONObject> page, @Param("userIds") List<Long> ids, @Param("sqlDateFormat") String sqlDateFormat, @Param("time") String time);
}
