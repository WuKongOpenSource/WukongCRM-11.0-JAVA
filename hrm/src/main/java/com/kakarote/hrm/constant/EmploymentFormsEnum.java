package com.kakarote.hrm.constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum EmploymentFormsEnum {

    /**
     * 聘用形式枚举
     */
    OFFICIAL(1,"正式"),NO_OFFICIAL(2,"非正式");

    EmploymentFormsEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    private int value;
    private String name;

    public static String  parseName(int type){
        for(EmploymentFormsEnum value : EmploymentFormsEnum.values()){
            if(value.value == type){
                return value.name;
            }
        }
        return "";
    }


    public static int  valueOfType(String name){
        for(EmploymentFormsEnum value : EmploymentFormsEnum.values()){
            if(value.name.equals(name)){
                return value.value;
            }
        }
        return -1;
    }

    public static List<Map<String,Object>> parseMap(){
        List<Map<String,Object>> mapList = new ArrayList<>();
        for(EmploymentFormsEnum value : EmploymentFormsEnum.values()){
            Map<String,Object> map = new HashMap<>();
            map.put("name",value.name);
            map.put("value",value.value);
            mapList.add(map);
        }
        return mapList;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
