package com.kakarote.hrm.constant;

public enum HrmActionTypeEnum {
    /**
     * 操作记录类型类型枚举
     */
    EMPLOYEE(1,"员工"),RECRUIT_POST(2,"招聘职位"),RECRUIT_CANDIDATE(3,"候选人"),
    EMPLOYEE_APPRAISAL(4,"员工绩效");

    HrmActionTypeEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }


    private String name;
    private int value;

    public static String  parseName(int type){
        for(HrmActionTypeEnum value : HrmActionTypeEnum.values()){
            if(value.value == type){
                return value.name;
            }
        }
        return "";
    }

    public static int  valueOfType(String name){
        for(HrmActionTypeEnum value : HrmActionTypeEnum.values()){
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
