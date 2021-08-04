package com.kakarote.hrm.constant;

import lombok.Getter;

@Getter
public enum NotesType {

    /**
     * 备忘类型
     */
    NOTES(1,"备忘"),BIRTHDAY(2,"生日"),ENTRY(3,"入职")
    ,BECOME(4,"转正"),LEAVE(5,"离职"),RECRUIT(6,"招聘"),
    CLOCK(7,"打卡");

    NotesType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    private int value;
    private String name;
}
