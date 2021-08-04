package com.kakarote.hrm.constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum  IdTypeEnum {
    /**
     * 证件类型枚举
     */
    ID_CARD(1,"身份证"),HONG_KONG_CARD(2,"港澳通行证"),TAI_WAN_CARD(3,"台湾通行证"),
    PASSPORT(4,"护照"),OTHER(5,"其他");

    IdTypeEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }


    private String name;
    private int value;

    public static String  parseName(int type){
        for(IdTypeEnum value : IdTypeEnum.values()){
            if(value.value == type){
                return value.name;
            }
        }
        return "";
    }

    public static int  valueOfType(String name){
        for(IdTypeEnum value : IdTypeEnum.values()){
            if(value.name.equals(name)){
                return value.value;
            }
        }
        return -1;
    }

    public static List<Map<String,Object>> parseMap(){
        List<Map<String,Object>> mapList = new ArrayList<>();
        for(IdTypeEnum value : IdTypeEnum.values()){
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
