package com.kakarote.bi.common;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author JiaS
 * @date 2020/9/14
 */
@Slf4j
public class BiPatch {

    private static final String DEFAULT_TABLE_NAME_PREFIX = "wk_crm_customer_stats_";

    /**
     * 补充字段数据
     *
     * @param jsonList
     * @param filedNames
     * @param timeList
     * @param timeFiledName
     * @return void
     * @date 2020/9/14 16:09
     **/
    public static void supplementJsonList(List<JSONObject> jsonList, String timeFiledName, List<Integer> timeList, String... filedNames) {
        if (jsonList != null) {
            List<Integer> list = jsonList.stream().map(jsonObject -> jsonObject.getInteger(timeFiledName)).collect(Collectors.toList());
            timeList.removeAll(list);
        } else {
            jsonList = new ArrayList<>();
        }
        for (Integer time : timeList) {
            JSONObject jsonObject = new JSONObject().fluentPut(timeFiledName, time);
            Arrays.asList(filedNames).forEach(filedName -> {
                if ("dealCustomerNum".equals(filedName)) {
                    jsonObject.put(filedName, null);
                } else {
                    jsonObject.put(filedName, 0);
                }
            });
            jsonList.add(jsonObject);
        }
    }


    /**
     * 自定义时间取年份或对应表名
     * @date 2020/9/17 18:46
     * @param beginTime
     * @param finalTime
     * @param isTableName
     * @return java.util.List<java.lang.String>
     **/
    public static List<String> getYearsOrTableNameList(Integer beginTime, Integer finalTime,boolean isTableName) {
        List<String> list = new ArrayList<>();
        if (beginTime == null || finalTime == null) {
            log.error("beginTime or finalTime must not be null !");
            return list;
        }
        if (beginTime < 1000 || finalTime < 1000) {
            log.error("beginTime or finalTime must be gt 1000 !");
            return list;
        }
        if (beginTime > finalTime) {
            log.error("finalTime must be gt beginTime !");
            return list;
        }
        String start = beginTime.toString().substring(0, 4);
        String end = finalTime.toString().substring(0, 4);
        for (int i = Integer.parseInt(start); i <= Integer.parseInt(end); i++) {
            list.add(isTableName ? DEFAULT_TABLE_NAME_PREFIX + i : String.valueOf(i));
        }
        return list;
    }
}
