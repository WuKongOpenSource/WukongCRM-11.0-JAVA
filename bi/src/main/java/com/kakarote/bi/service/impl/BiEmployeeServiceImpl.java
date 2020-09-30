package com.kakarote.bi.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.bi.common.BiPatch;
import com.kakarote.bi.mapper.BiEmployeeMapper;
import com.kakarote.bi.service.BiEmployeeService;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.crm.entity.BiParams;
import com.kakarote.core.utils.BiTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class BiEmployeeServiceImpl implements BiEmployeeService {

    @Autowired
    private BiEmployeeMapper biEmployeeMapper;

    /**
     * 合同数量分析
     *
     * @param biParams params
     * @return data
     */
    @Override
    public List<JSONObject> contractNumStats(BiParams biParams) {
        Integer menuId = 106;
        biParams.setMenuId(menuId);
        String type = biParams.getType();
        Integer year = biParams.getYear();
        BiTimeUtil.BiTimeEntity record = BiTimeUtil.analyzeType(biParams);
        int cycleNum = 12;
        Integer beginTime = year * 100 + 1;
        List<Integer> timeList = new ArrayList<>();
        for (int i = 1; i <= cycleNum; i++) {
            timeList.add(beginTime);
            beginTime = BiTimeUtil.estimateTime(beginTime);
        }
        List<JSONObject> recordList;
        Map<String, Object> map = record.toMap();
        map.put("timeList", timeList);
        if ("contractNum".equals(type)) {
            recordList = biEmployeeMapper.contractNum(map);
        } else if ("contractMoney".equals(type)) {
            recordList = biEmployeeMapper.contractMoney(map);
        } else if ("receivables".equals(type)) {
            recordList = biEmployeeMapper.receivables(map);
        } else {
            throw new CrmException(SystemCodeEnum.SYSTEM_NO_VALID);
        }
        recordList.forEach(r -> {
            BigDecimal thisMonth = r.getBigDecimal("thisMonth");
            BigDecimal lastMonth = r.getBigDecimal("lastMonth");
            BigDecimal lastYear = r.getBigDecimal("lastYear");
            r.put("lastMonthGrowth", thisMonth.compareTo(new BigDecimal(0)) != 0 && lastMonth.compareTo(new BigDecimal(0)) != 0 ? (thisMonth.subtract(lastMonth)).multiply(new BigDecimal(100)).divide(lastMonth, 2, BigDecimal.ROUND_HALF_UP) : 0);
            r.put("lastYearGrowth", thisMonth.compareTo(new BigDecimal(0)) != 0 && lastYear.compareTo(new BigDecimal(0)) != 0 ? (thisMonth.subtract(lastYear)).multiply(new BigDecimal(100)).divide(lastYear, 2, BigDecimal.ROUND_HALF_UP) : 0);
            r.remove("lastMonth");
            r.remove("lastYear");
        });
        return recordList;
    }

    /**
     * 合同汇总表
     *
     * @param biParams params
     * @return data
     */
    @Override
    public JSONObject totalContract(BiParams biParams) {
        Integer menuId = 106;
        biParams.setMenuId(menuId);
        BiTimeUtil.BiTimeEntity record = BiTimeUtil.analyzeType(biParams);
        Integer beginTime = record.getBeginTime();
        Integer cycleNum = record.getCycleNum();
        JSONObject total = biEmployeeMapper.totalContract(record);
        List<Integer> timeList = new ArrayList<>();
        for (int i = 1; i <= cycleNum; i++) {
            timeList.add(beginTime);
            beginTime = BiTimeUtil.estimateTime(beginTime);
        }
        Map<String, Object> map = record.toMap();
        map.put("timeList", timeList);
        List<JSONObject> recordList = biEmployeeMapper.totalContractTable(map);
        BiPatch.supplementJsonList(recordList,"type",timeList, "contractNum","contractMoney","receivablesMoney");
        return total.fluentPut("list", recordList);
    }

    /**
     * 发票统计
     *
     * @param biParams params
     * @return data
     */
    @Override
    public JSONObject invoiceStats(BiParams biParams) {
        Integer menuId = 106;
        biParams.setMenuId(menuId);
        Integer year = biParams.getYear();
        BiTimeUtil.BiTimeEntity record = BiTimeUtil.analyzeType(biParams);
        int cycleNum = 12;
        Integer beginTime = year * 100 + 1;
        List<Integer> timeList = new ArrayList<>();
        for (int i = 1; i <= cycleNum; i++) {
            timeList.add(beginTime);
            beginTime = BiTimeUtil.estimateTime(beginTime);
        }
        JSONObject total = biEmployeeMapper.totalInvoice(year, record.getUserIds());
        Map<String, Object> map = record.toMap();
        map.put("timeList", timeList);
        List<JSONObject> objectList = biEmployeeMapper.invoiceStatsTable(map);
        BiPatch.supplementJsonList(objectList,"type",timeList,
                "receivablesMoney","receivablesNoInvoice","invoiceMoney","invoiceNoReceivables");
        return total.fluentPut("list", objectList);
    }
}
