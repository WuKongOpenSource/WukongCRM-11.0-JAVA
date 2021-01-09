package com.kakarote.bi.service;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.utils.BiTimeUtil;

import java.util.List;

/**
 * @author JiaS
 * @date 2020/12/1
 */
public interface BiEsStatisticsService {

    /**
     * es统计客户数量
     * @date 2020/12/1 10:58
     * @param timeEntity
     * @param isNeedDealNum
     * @return java.util.List<com.alibaba.fastjson.JSONObject>
     **/
    List<JSONObject> getStatisticsCustomerInfo(BiTimeUtil.BiTimeEntity timeEntity,boolean isNeedDealNum);


    /**
     * 统计结果合并
     * @date 2020/12/1 10:58
     * @param customerNumList
     * @param dealNumList
     * @return java.util.List<com.alibaba.fastjson.JSONObject>
     **/
    public List<JSONObject> mergeJsonObjectList(List<JSONObject> customerNumList,List<JSONObject> dealNumList);
}
