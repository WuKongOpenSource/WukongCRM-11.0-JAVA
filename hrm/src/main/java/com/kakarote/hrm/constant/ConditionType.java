package com.kakarote.hrm.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConditionType {
    /**
     * 高级筛选条件类型
     */
    IS("等于","is",1),
    IS_NOT("不等于","isNot",2),
    CONTAINS("包含","contains",3),
    NOT_CONTAINS("不包含","notContains",4),
    IS_NULL("为空","isNull",5),
    IS_NOT_NULL("不为空","isNotNull",6),
    GT("大于","gt",7),
    EGT("大于等于","egt",8),
    LT("小于","lt",9),
    ELT("小于等于","elt",10),
    DATE("开始结束","date",11),
    DATETIME("开始结束","datetime",12),
    IN("包含","in",13),
    NOT_IN("不包含","notIn",14);



    private String name;
    private String condition;
    private int conditionType;


    public static ConditionType  parse(int conditionType){
        for(ConditionType value : ConditionType.values()){
            if(value.conditionType == conditionType){
                return value;
            }
        }
        return IS;
    }




}
