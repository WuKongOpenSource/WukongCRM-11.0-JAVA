package com.kakarote.hrm.constant.achievement;


public enum AppraisalStatus {
    /**
     * 绩效状态 0 未开启考核 1 绩效填写中 2 绩效评定中 3 结果确认中 4 归档
     */
    UNOPENED(0,"未开启考核"),FILLING_IN(1,"绩效填写中"),
    UNDER_EVALUATION(2,"绩效评定中"),CONFIRMING(3,"结果确认中"),
    ARCHIVE(4,"归档");

    AppraisalStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }


    private String name;
    private int value;

    public static String  parseName(int type){
        for(AppraisalStatus value : AppraisalStatus.values()){
            if(value.value == type){
                return value.name;
            }
        }
        return "";
    }

    public static AppraisalStatus  parse(int type){
        for(AppraisalStatus value : AppraisalStatus.values()){
            if(value.value == type){
                return value;
            }
        }
        return FILLING_IN;
    }

    public static int  valueOfType(String name){
        for(AppraisalStatus value : AppraisalStatus.values()){
            if(value.name.equals(name)){
                return value.value;
            }
        }
        return -1;
    }


    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }
}
