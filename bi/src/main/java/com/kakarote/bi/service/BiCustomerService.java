package com.kakarote.bi.service;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.crm.entity.BiParams;

import java.util.List;

public interface BiCustomerService {

    /**
     * 查询客户总量分析图
     * @param biParams params
     * @return data
     */
    public List<JSONObject> totalCustomerStats(BiParams biParams);

    /**
     * 查询客户总量分析图
     * @param biParams params
     * @return data
     */
    public JSONObject totalCustomerTable(BiParams biParams);

    /**
     * 客户跟进次数分析
     * @param biParams params
     * @return data
     */
    public List<JSONObject> customerRecordStats(BiParams biParams);

    /**
     * 客户跟进次数分析表
     * @param biParams params
     * @return data
     */
    public JSONObject customerRecordInfo(BiParams biParams);

    /**
     * 客户跟进方式分析表
     * @param biParams params
     * @return data
     */
    public List<JSONObject> customerRecodCategoryStats(BiParams biParams);

    /**
     * 客户转化率分析图
     * @param biParams params
     * @return data
     */
    public List<JSONObject> customerConversionStats(BiParams biParams);

    /**
     * 客户转化率分析表
     * @param biParams params
     * @return data
     */
    public BasePage<JSONObject> customerConversionInfo(BiParams biParams);

    /**
     * 公海客户分析图
     * @param biParams params
     * @return data
     */
    public List<JSONObject> poolStats(BiParams biParams);

    /**
     * 公海客户分析表
     * @param biParams params
     * @return data
     */
    public JSONObject poolTable(BiParams biParams);

    /**
     * 员工客户成交周期图
     * @param biParams params
     * @return data
     */
    public List<JSONObject> employeeCycle(BiParams biParams);

    /**
     * 员工客户成交周期表
     * @param biParams params
     * @return data
     */
    public JSONObject employeeCycleInfo(BiParams biParams);

    /**
     * 地区成交周期图
     * @param biParams params
     * @return data
     */
    public JSONObject districtCycle(BiParams biParams);

    /**
     * 产品成交周期图
     * @param biParams params
     * @return data
     */
    public JSONObject productCycle(BiParams biParams);

    /**
     * 客户满意度分析
     * @param biParams params
     * @return data
     */
    public List<JSONObject> customerSatisfactionTable(BiParams biParams);

    /**
     * 产品满意度分析
     * @param biParams params
     * @return data
     */
    public List<JSONObject> productSatisfactionTable(BiParams biParams);
}
