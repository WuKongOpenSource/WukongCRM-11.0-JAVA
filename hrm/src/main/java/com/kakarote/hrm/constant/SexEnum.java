package com.kakarote.hrm.constant;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public enum SexEnum {
    /**
     * 性别枚举
     */
    UNFILLED(-1,"未填写"),MAN(1,"男"),WOMAN(2,"女");

    private int value;
    private String name;

    SexEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String  parseName(int type){
        for(SexEnum value : SexEnum.values()){
            if(value.value == type){
                return value.name;
            }
        }
        return "";
    }


    public static int  valueOfType(String name){
        for(SexEnum value : SexEnum.values()){
            if(value.name.equals(name)){
                return value.value;
            }
        }
        return -1;
    }

    public static List<Map<String,Object>> parseMap(){
        List<Map<String,Object>> mapList = new ArrayList<>();
        for(SexEnum value : SexEnum.values()){
            Map<String,Object> map = new HashMap<>();
            map.put("name",value.name);
            map.put("value",value.value);
            mapList.add(map);
        }
        return mapList;
    }
}
