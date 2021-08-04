package com.kakarote.hrm.constant;

public enum  FiledIsFixedEnum {
    /**
     * 是否是固定字段
     */
    FIXED(1,"固定字段"),NO_FIXED(0,"自定义");
    FiledIsFixedEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }


    private String name;
    private int value;

    public static String  parseName(int type){
        for(FiledIsFixedEnum value : FiledIsFixedEnum.values()){
            if(value.value == type){
                return value.name;
            }
        }
        return "";
    }

    public static int  valueOfType(String name){
        for(FiledIsFixedEnum value : FiledIsFixedEnum.values()){
            if(value.name.equals(name)){
                return value.value;
            }
        }
        return -1;
    }

    public static FiledIsFixedEnum  parse(int type){
        for(FiledIsFixedEnum value : FiledIsFixedEnum.values()){
            if(value.value == type){
                return value;
            }
        }
        return FIXED;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }
}
