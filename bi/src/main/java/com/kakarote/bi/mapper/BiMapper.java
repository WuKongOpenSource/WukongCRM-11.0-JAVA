package com.kakarote.bi.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.kakarote.bi.entity.VO.ProductStatisticsVO;
import com.kakarote.core.utils.BiTimeUtil;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BiMapper {

    @SqlParser(filter = true)
    public List<ProductStatisticsVO> queryProductSell(BiTimeUtil.BiTimeEntity entity);

    public JSONObject queryProductSellCount(BiTimeUtil.BiTimeEntity entity);

    @SqlParser(filter = true)
    public List<JSONObject> taskCompleteStatistics(JSONObject entity);
}
