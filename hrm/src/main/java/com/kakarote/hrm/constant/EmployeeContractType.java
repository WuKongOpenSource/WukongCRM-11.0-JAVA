package com.kakarote.hrm.constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum EmployeeContractType {
    /**
     * 1、固定期限劳动合同 2、无固定期限劳动合同 3、已完成一定工作任务为期限的劳动合同 4、实习协议 5、劳务合同 6、返聘协议 7、劳务派遣合同 8、借调合同 9、其他
     */
    FIXED_TERM_LABOR_CONTRACT(1,"固定期限劳动合同"),NO_FIXED_TERM_LABOR_CONTRACT(2,"无固定期限劳动合同")
    ,WORK_TASK_LABOR_CONTRACT(3,"已完成一定工作任务为期限的劳动合同"),INTERNSHIP_AGREEMENT(4,"实习协议")
    ,LABOR_CONTRACT(5,"劳务合同"),REEMPLOYMENT_AGREEMENT(6,"返聘协议"),LABOR_DISPATCH_CONTRACT(7,"劳务派遣合同")
    ,SECONDMENT_CONTRACT(8,"借调合同"),OTHER(9,"其他")

    ;


    EmployeeContractType(int value, String name) {
        this.value = value;
        this.name = name;
    }


    private String name;
    private int value;

    public static String  parseName(int type){
        for(EmployeeContractType value : EmployeeContractType.values()){
            if(value.value == type){
                return value.name;
            }
        }
        return "";
    }

    public static int  valueOfType(String name){
        for(EmployeeContractType value : EmployeeContractType.values()){
            if(value.name.equals(name)){
                return value.value;
            }
        }
        return -1;
    }

    public static List<Map<String,Object>> parseMap(){
        List<Map<String,Object>> mapList = new ArrayList<>();
        for(EmployeeContractType value : EmployeeContractType.values()){
            Map<String,Object> map = new HashMap<>();
            map.put("name",value.name);
            map.put("value",value.value);
            mapList.add(map);
        }
        return mapList;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }
}
