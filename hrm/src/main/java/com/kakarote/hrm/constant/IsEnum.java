package com.kakarote.hrm.constant;

public enum  IsEnum {
    /**
     * 是否类型枚举  0 否 1 是
     */
    YES(1,"是"),NO(0,"否");

    IsEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }


    private String name;
    private int value;

    public static String  parseName(int type){
        for(IsEnum value : IsEnum.values()){
            if(value.value == type){
                return value.name;
            }
        }
        return "";
    }

    public static int  valueOfType(String name){
        for(IsEnum value : IsEnum.values()){
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
