package com.kakarote.hrm.constant;

public enum CandidateEducationEnum {

    //    1小学 2初中 3高中 4大专 5本科 6硕士 7博士
    PRIMARY_SCHOOL(1,"小学"),JUNIOR_HIGH_SCHOOL(2,"初中"),
    HIGH_SCHOOL(3,"高中"),JUNIOR_COLLEGE(4,"大专"),
    UNDERGRADUATE(5,"本科"),MASTER(6,"硕士"),
    DOCTOR(7,"博士");

     CandidateEducationEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    private int value;
    private String name;

    public static String  parseName(int type){
        for(CandidateEducationEnum value : CandidateEducationEnum.values()){
            if(value.value == type){
                return value.name;
            }
        }
        return "";
    }


    public static int  valueOfType(String name){
        for(CandidateEducationEnum value : CandidateEducationEnum.values()){
            if(value.name.equals(name)){
                return value.value;
            }
        }
        return -1;
    }

}
