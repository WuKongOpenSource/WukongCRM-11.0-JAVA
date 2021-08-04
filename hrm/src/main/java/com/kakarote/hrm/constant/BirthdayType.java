package com.kakarote.hrm.constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum BirthdayType {
    /**
     * 生日类型枚举
     */
    SOLAR_CALENDAR(1,"阳历"),LUNAR_CALENDAR(2,"农历");

    BirthdayType(int value, String name) {
        this.value = value;
        this.name = name;
    }


    private String name;
    private int value;

    public static String  parseName(int type){
        for(BirthdayType value : BirthdayType.values()){
            if(value.value == type){
                return value.name;
            }
        }
        return "";
    }

    public static int  valueOfType(String name){
        for(BirthdayType value : BirthdayType.values()){
            if(value.name.equals(name)){
                return value.value;
            }
        }
        return -1;
    }

    public static List<Map<String,Object>> parseMap(){
        List<Map<String,Object>> mapList = new ArrayList<>();
        for(BirthdayType value : BirthdayType.values()){
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
