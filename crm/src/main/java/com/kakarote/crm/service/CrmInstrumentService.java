package com.kakarote.crm.service;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.crm.entity.BiParams;
import com.kakarote.crm.entity.BO.CrmSearchParamsBO;
import com.kakarote.crm.entity.PO.CrmActivity;

import java.util.List;
import java.util.Map;

public interface CrmInstrumentService {

    /**
     * 销售简报
     * @param biParams params
     * @return data
     */
    public JSONObject queryBulletin(BiParams biParams);

    /**
     * 销售简报的详情
     * @param biParams 参数
     * @return data
     */
    public BasePage<Map<String, Object>> queryBulletinInfo(BiParams biParams);

    /**
     * 遗忘客户统计
     * @param biParams params
     * @return data
     */
    public JSONObject forgottenCustomerCount(BiParams biParams);

    /**
     * 销售漏斗
     * @param biParams params
     * @return data
     */
    public JSONObject sellFunnel(BiParams biParams);


    BasePage<Map<String, Object>> sellFunnelBusinessList(CrmSearchParamsBO crmSearchParamsBO);

    /**
     * 销售趋势
     * @param biParams params
     * @return data
     */
    public JSONObject salesTrend(BiParams biParams);

    /**
     * 数据汇总
     * @param biParams params
     * @return data
     */
    public JSONObject queryDataInfo(BiParams biParams);

    /**
     * 业绩指标
     */
    public JSONObject queryPerformance(BiParams biParams);

    BasePage<CrmActivity> queryRecordList(BiParams biParams);

    List<JSONObject> queryRecordCount(BiParams biParams);

    BasePage<Map<String, Object>> forgottenCustomerPageList(BiParams biParams);

    BasePage<Map<String, Object>> unContactCustomerPageList(BiParams biParams);

}
