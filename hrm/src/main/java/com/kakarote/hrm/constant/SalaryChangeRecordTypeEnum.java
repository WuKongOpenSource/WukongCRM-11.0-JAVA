package com.kakarote.hrm.constant;

public enum SalaryChangeRecordTypeEnum {
    /**
     * 调薪记录类型 1 定薪 2 调薪
     */
    FIX_SALARY(1,"定薪"),CHANGE_SALARY(2,"调薪")
    ;

    SalaryChangeRecordTypeEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }


    private String name;
    private int value;

    public static String  parseName(int type){
        for(SalaryChangeRecordTypeEnum value : SalaryChangeRecordTypeEnum.values()){
            if(value.value == type){
                return value.name;
            }
        }
        return "";
    }

    public static int  valueOfType(String name){
        for(SalaryChangeRecordTypeEnum value : SalaryChangeRecordTypeEnum.values()){
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
