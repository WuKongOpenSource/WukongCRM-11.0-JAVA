package com.kakarote.hrm.constant;

/**
 * 合同状态
 */
public enum  EmployeeContractStatus {
    /**
     * 合同状态 执行中、已到期、未执行
     */
    NOT_PERFORMED(0,"未执行"),IN_PROGRESS(1,"执行中"),BE_EXPIRED(2,"已到期");

    EmployeeContractStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }


    private String name;
    private int value;

    public static String  parseName(int type){
        for(EmployeeContractStatus value : EmployeeContractStatus.values()){
            if(value.value == type){
                return value.name;
            }
        }
        return "";
    }

    public static int  valueOfType(String name){
        for(EmployeeContractStatus value : EmployeeContractStatus.values()){
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
