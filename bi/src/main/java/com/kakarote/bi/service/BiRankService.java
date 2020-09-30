package com.kakarote.bi.service;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.feign.crm.entity.BiParams;

import java.util.List;

public interface BiRankService {

    /**
     * 城市分布分析
     * @param biParams params
     * @return data
     */
    public List<JSONObject> addressAnalyse(BiParams biParams);

    /**
     * 城市分布分析
     * @param biParams params
     * @return data
     */
    public List<JSONObject> portrait(BiParams biParams);

    /**
     * 城市级别分析
     * @param biParams params
     * @return data
     */
    public List<JSONObject> portraitLevel(BiParams biParams);

    /**
     * 城市来源分析
     * @param biParams params
     * @return data
     */
    public List<JSONObject> portraitSource(BiParams biParams);

    /**
     * 产品分类销量分析
     *
     * @param biParams params
     * @return data
     */
    public List<JSONObject> contractProductRanKing(BiParams biParams);

    /**
     * 合同金额排行榜
     * @param biParams params
     * @return data
     */
    public List<JSONObject> contractRanKing(BiParams biParams);

    /**
     * 回款金额排行榜
     * @param biParams params
     * @return data
     */
    public List<JSONObject> receivablesRanKing(BiParams biParams);

    /**
     * 签约合同排行榜
     * @param biParams params
     * @return data
     */
    public List<JSONObject> contractCountRanKing(BiParams biParams);

    /**
     * 产品销量排行榜
     * @param biParams params
     * @return data
     */
    public List<JSONObject> productCountRanKing(BiParams biParams);

    /**
     * 新增客户数排行榜
     * @param biParams params
     * @return data
     */
    public List<JSONObject> customerCountRanKing(BiParams biParams);

    /**
     * 新增联系人排行榜
     * @param biParams params
     * @return data
     */
    public List<JSONObject> contactsCountRanKing(BiParams biParams);

    /**
     * 排行榜
     * @param biParams params
     * @return data
     */
    public JSONObject ranking(BiParams biParams);

    /**
     * 跟进客户数排行榜
     * @param biParams params
     * @return data
     */
    public List<JSONObject> customerGenjinCountRanKing(BiParams biParams);

    /**
     * 跟进次数排行榜
     * @param biParams params
     * @return data
     */
    public List<JSONObject> recordCountRanKing(BiParams biParams);

    /**
     * 出差次数排行
     * @param biParams params
     * @return data
     */
    public List<JSONObject> travelCountRanKing(BiParams biParams);


}
