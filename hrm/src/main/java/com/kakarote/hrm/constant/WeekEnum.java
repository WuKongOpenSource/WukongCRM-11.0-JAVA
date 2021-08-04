package com.kakarote.hrm.constant;

import java.util.TreeMap;

public enum  WeekEnum{
    /**
     * 工作日中英文对应
     */
    MONDAY(1,"monday","一"),
    TUESDAY(2,"tuesday","二"),
    WEDNESDAY(3,"wednesday","三"),
    THURSDAY(4,"thursday","四"),
    FRIDAY(5,"friday","五"),
    SATURDAY(6,"saturday","六"),
    SUNDAY(7,"sunday","七");


    WeekEnum(Integer order,String date, String name){
        this.order = order;
        this.date = date;
        this.name = name;
    }
    private final Integer order;
    private final String date;
    private final String name;

    public static TreeMap<Integer,String> getMapName(String date){
        TreeMap<Integer,String> treeMap = new TreeMap<>();
        for(WeekEnum value : WeekEnum.values()){
            if(value.date.equals(date)){
                treeMap.put(value.order,value.name);
                return treeMap;
            }
        }
        return null;
    }

    public static String  getName(String date){
        for(WeekEnum value : WeekEnum.values()){
            if(value.date.equals(date)){
                return value.name;
            }
        }
        return "";
    }

    public static String getDayOfWeek(Integer order){
        for (WeekEnum value : WeekEnum.values()){
            if (value.order.equals( order)){
                return value.date;
            }
        }
        return null;
    }
}
