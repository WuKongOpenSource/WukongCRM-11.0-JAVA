package com.kakarote.hrm.constant;

import lombok.Getter;

/**
 * 员工异动类型枚举
 */
@Getter
public enum AbnormalChangeType {
    //    异动类型 1 入职 2 离职 3 转正 4 调岗
    NEW_ENTRY(1,"新入职"),RESIGNATION(2,"离职"),
    BECOME(3,"转正"),CHANGE_POST(4,"调岗")
    ;



    AbnormalChangeType(int value, String name) {
        this.value = value;
        this.name = name;
    }


    private String name;
    private int value;
}
