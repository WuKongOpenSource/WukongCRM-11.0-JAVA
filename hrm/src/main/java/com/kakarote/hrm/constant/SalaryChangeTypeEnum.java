package com.kakarote.hrm.constant;

public enum SalaryChangeTypeEnum {
    /**
     * 调薪状态 0 未定薪 1 已定薪 2 已调薪
     */
    NOT_FIX_SALARY(0,"未定薪"),HAS_FIX_SALARY(1,"已定薪"),HAS_CHANGE_SALARY(2,"已调薪")
    ;

    SalaryChangeTypeEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }


    private String name;
    private int value;

    public static String  parseName(int type){
        for(SalaryChangeTypeEnum value : SalaryChangeTypeEnum.values()){
            if(value.value == type){
                return value.name;
            }
        }
        return "";
    }

    public static int  valueOfType(String name){
        for(SalaryChangeTypeEnum value : SalaryChangeTypeEnum.values()){
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
