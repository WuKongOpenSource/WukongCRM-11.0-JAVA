package com.kakarote.hrm.constant;

/**
 * 教育方式枚举
 */
public enum TeachingMethodsEnum {
    /**
     * 系统默认 :1 全日制、2成人教育、3远程教育、4自学考试、5其他
     */
    FULL_TIME(1,"全日制"),ADULT_EDUCATION(2,"成人教育"),DISTANCE_EDUCATION(3,"远程教育"),
    SELF_EXAMINATION(4,"自学考试"),OTHER(5,"其他");

    TeachingMethodsEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }


    private String name;
    private int value;

    public static String  parseName(int type){
        for(TeachingMethodsEnum value : TeachingMethodsEnum.values()){
            if(value.value == type){
                return value.name;
            }
        }
        return "";
    }

    public static int  valueOfType(String name){
        for(TeachingMethodsEnum value : TeachingMethodsEnum.values()){
            if(value.name.equals(name)){
                return value.value;
            }
        }
        return -1;
    }
}
