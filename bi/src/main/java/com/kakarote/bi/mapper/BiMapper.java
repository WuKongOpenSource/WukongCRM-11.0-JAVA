package com.kakarote.bi.mapper;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.utils.BiTimeUtil;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BiMapper {

    public List<JSONObject> queryProductSell(BiTimeUtil.BiTimeEntity entity);

    public List<JSONObject> taskCompleteStatistics(JSONObject entity);
}
