package com.kakarote.hrm.constant;

public enum PostEducationEnum {

    //    1不限 2高中及以上 3大专及以上 4本科及以上 5硕士及以上 6博士
    UNLIMITED(1,"不限"), HIGH_SCHOOL_UP(2,"高中及以上"),JUNIOR_COLLEGE_UP(3,"大专及以上"),
    UNDERGRADUATE_UP(4,"本科及以上"),MASTER_UP(5,"硕士及以上"),
    DOCTOR(6,"博士");

     PostEducationEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    private int value;
    private String name;

    public static String  parseName(int type){
        for(PostEducationEnum value : PostEducationEnum.values()){
            if(value.value == type){
                return value.name;
            }
        }
        return "";
    }


    public static int  valueOfType(String name){
        for(PostEducationEnum value : PostEducationEnum.values()){
            if(value.name.equals(name)){
                return value.value;
            }
        }
        return -1;
    }

}
