package com.kakarote.hrm.constant;

import lombok.Getter;

@Getter
public enum  EmployeeEntryStatus {

    IN(1,"在职"),TO_IN(2,"待入职"),TO_LEAVE(3,"待离职"),ALREADY_LEAVE(4,"已离职");

    EmployeeEntryStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }

    private int value;
    private String name;
}
