package com.kakarote.hrm.constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum EmployeeEducationEnum {

    //    1小学、2初中、3中专、4中职、5中技、6高中、7大专、8本科、9硕士、10博士、11博士后、12其他
    PRIMARY_SCHOOL(1,"小学"),JUNIOR_HIGH_SCHOOL(2,"初中"),
    TECHNICAL_SECONDARY_SCHOOL(3,"中专"),VOCATIONAL(4,"中职"),ZHONGJI(5,"中技"),
    HIGH_SCHOOL(6,"高中"),JUNIOR_COLLEGE(7,"大专"),
    UNDERGRADUATE(8,"本科"),MASTER(9,"硕士"),
    DOCTOR(10,"博士"),POSTDOCTOR(11,"博士后"),OTHER(12,"其他");

     EmployeeEducationEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    private int value;
    private String name;

    public static String  parseName(int type){
        for(EmployeeEducationEnum value : EmployeeEducationEnum.values()){
            if(value.value == type){
                return value.name;
            }
        }
        return "";
    }


    public static int  valueOfType(String name){
        for(EmployeeEducationEnum value : EmployeeEducationEnum.values()){
            if(value.name.equals(name)){
                return value.value;
            }
        }
        return -1;
    }

    public static List<Map<String,Object>> parseMap(){
        List<Map<String,Object>> mapList = new ArrayList<>();
        for(EmployeeEducationEnum value : EmployeeEducationEnum.values()){
            Map<String,Object> map = new HashMap<>();
            map.put("name",value.name);
            map.put("value",value.value);
            mapList.add(map);
        }
        return mapList;
    }
}
