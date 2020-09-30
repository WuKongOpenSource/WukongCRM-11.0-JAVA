package com.kakarote.bi.service;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.feign.crm.entity.BiParams;

import java.util.List;

public interface BiEmployeeService {
    /**
     * 合同数量分析
     * @param biParams params
     * @return data
     */
    public List<JSONObject> contractNumStats(BiParams biParams);

    /**
     * 合同汇总表
     * @param biParams params
     * @return data
     */
    public JSONObject totalContract(BiParams biParams);

    /**
     * 发票统计
     * @param biParams params
     * @return data
     */
    public JSONObject invoiceStats(BiParams biParams);
}
