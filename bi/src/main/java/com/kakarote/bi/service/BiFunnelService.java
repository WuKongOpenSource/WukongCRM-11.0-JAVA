package com.kakarote.bi.service;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.crm.entity.BiParams;

import java.util.List;

public interface BiFunnelService {
    /**
     * 销售漏斗
     * @param biParams params
     * @return data
     */
    public List<JSONObject> sellFunnel(BiParams biParams);

    /**
     * 新增商机分析图
     * @param biParams params
     * @return data
     */
    public List<JSONObject> addBusinessAnalyze(BiParams biParams);

    /**
     * 新增商机分析表
     * @param biParams params
     * @return data
     */
    public BasePage<JSONObject> sellFunnelList(BiParams biParams);

    /**
     * 商机转化率分析
     * @param biParams params
     * @return data
     */
    public List<JSONObject> win(BiParams biParams);

}
