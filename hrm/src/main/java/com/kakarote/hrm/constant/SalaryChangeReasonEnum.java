package com.kakarote.hrm.constant;

public enum SalaryChangeReasonEnum {
    /**
     * 调薪原因 1 入职核定 2 转正 3 晋升 4 调动 5 年中调薪 6 年度调薪 7 特别调薪 8 其他
     */
    ENTRY_FIX_SALARY(0,"入职定薪"),ENTRY_APPROVAL(1,"入职核定"),BECOME(2,"转正"),PROMOTION(3,"晋升"),
    TRANSFER(4,"调动"),MID_YEAR_SALARY_CHANGE(5,"年中调薪"),YEAR_SALARY_CHANGE(6,"年度调薪"),
    SPECIAL_SALARY_CHANGE(7,"特别调薪"),OTHER(8,"其他"),

    ;

    SalaryChangeReasonEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }


    private String name;
    private int value;

    public static String  parseName(int type){
        for(SalaryChangeReasonEnum value : SalaryChangeReasonEnum.values()){
            if(value.value == type){
                return value.name;
            }
        }
        return "";
    }

    public static int  valueOfType(String name){
        for(SalaryChangeReasonEnum value : SalaryChangeReasonEnum.values()){
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
