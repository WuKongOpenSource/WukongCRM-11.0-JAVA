package com.kakarote.hrm.constant;

public enum SalaryChangeRecordStatusEnum {
    /**
     * 调薪记录状态 0 未生效 1 已生效 2 已取消
     */
    NOT_TAKE_EFFECT(0,"未生效"),HAS_TAKE_EFFECT(1,"已生效"),CANCEL(2,"已取消")
    ;

    SalaryChangeRecordStatusEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }


    private String name;
    private int value;

    public static String  parseName(int type){
        for(SalaryChangeRecordStatusEnum value : SalaryChangeRecordStatusEnum.values()){
            if(value.value == type){
                return value.name;
            }
        }
        return "";
    }

    public static int  valueOfType(String name){
        for(SalaryChangeRecordStatusEnum value : SalaryChangeRecordStatusEnum.values()){
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
