package com.kakarote.bi.common;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author JiaS
 * @date 2020/9/14
 */
public class BiPatch {

    /**
     * 补充字段数据
     * @date 2020/9/14 16:09
     * @param jsonList
     * @param filedNames
     * @param timeList
     * @param timeFiledName
     * @return void
     **/
    public static void supplementJsonList(List<JSONObject> jsonList,String timeFiledName, List<Integer> timeList,String... filedNames){
        if (jsonList != null) {
            List<Integer> list = jsonList.stream().map(jsonObject -> jsonObject.getInteger(timeFiledName)).collect(Collectors.toList());
            timeList.removeAll(list);
        }else {
            jsonList = new ArrayList<>();
        }
        for (Integer time : timeList) {
            JSONObject jsonObject = new JSONObject().fluentPut(timeFiledName, time);
            Arrays.asList(filedNames).forEach(filedName -> {
                if ("dealCustomerNum".equals(filedName)){
                    jsonObject.put(filedName, null);
                }else {
                    jsonObject.put(filedName, 0.0);
                }
            });
            jsonList.add(jsonObject);
        }
    }
}
