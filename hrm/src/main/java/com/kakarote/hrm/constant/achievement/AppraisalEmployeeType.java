package com.kakarote.hrm.constant.achievement;


public enum AppraisalEmployeeType {
    /**
     * 1 员工本人 2 直属上级 3 所在部门负责人 4 上级部门负责人 5 指定目标确认人
     */
    MYSELF(1,"员工本人"),PARENT(2,"直属上级"),
    MYSELF_DEPT_MAIN(3,"所在部门负责人"),PARENT_DEPT_MAIN(4,"上级部门负责人"),
    DESIGNATION(5,"指定目标确认人");

    AppraisalEmployeeType(int value, String name) {
        this.value = value;
        this.name = name;
    }


    private String name;
    private int value;

    public static String  parseName(int type){
        for(AppraisalEmployeeType value : AppraisalEmployeeType.values()){
            if(value.value == type){
                return value.name;
            }
        }
        return "";
    }

    public static AppraisalEmployeeType parse(int type){
        for(AppraisalEmployeeType value : AppraisalEmployeeType.values()){
            if(value.value == type){
                return value;
            }
        }
        return MYSELF;
    }

    public static int  valueOfType(String name){
        for(AppraisalEmployeeType value : AppraisalEmployeeType.values()){
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
