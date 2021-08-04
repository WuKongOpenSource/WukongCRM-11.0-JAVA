package com.kakarote.hrm.constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum InterviewType {
    /**
     * 面试方式枚举  1现场面试 2电话面试 3视频面试
     */
    SITE(1,"现场面试"),PHONE(2,"电话面试"),VIDEO(3,"视频面试");

    InterviewType(int value, String name) {
        this.value = value;
        this.name = name;
    }


    private String name;
    private int value;

    public static String  parseName(int type){
        for(InterviewType value : InterviewType.values()){
            if(value.value == type){
                return value.name;
            }
        }
        return "";
    }

    public static int  valueOfType(String name){
        for(InterviewType value : InterviewType.values()){
            if(value.name.equals(name)){
                return value.value;
            }
        }
        return -1;
    }

    public static List<Map<String,Object>> parseMap(){
        List<Map<String,Object>> mapList = new ArrayList<>();
        for(InterviewType value : InterviewType.values()){
            Map<String,Object> map = new HashMap<>();
            map.put("name",value.name);
            map.put("value",value.value);
            mapList.add(map);
        }
        return mapList;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }
}
