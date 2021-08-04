package com.kakarote.hrm.constant.achievement;

public enum EmployeeAppraisalStatus {
    /**
     * 考核状态 1 待填写 2 待目标确认 3 待评定 4 待结果确认 5 终止绩效
     */
    TO_BE_FILLED(1,"待填写"),PENDING_CONFIRMATION(2,"待目标确认"),
    TO_BE_ASSESSED(3,"待结果评定"),TO_BE_CONFIRMED(4,"待结果确认"),
    STOP(5,"终止绩效"),COMPLETE(6,"考核完成");

    EmployeeAppraisalStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }


    private String name;
    private int value;

    public static String  parseName(int type){
        for(EmployeeAppraisalStatus value : EmployeeAppraisalStatus.values()){
            if(value.value == type){
                return value.name;
            }
        }
        return "";
    }

    public static int  valueOfType(String name){
        for(EmployeeAppraisalStatus value : EmployeeAppraisalStatus.values()){
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
