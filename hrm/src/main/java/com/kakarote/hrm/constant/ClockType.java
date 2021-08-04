package com.kakarote.hrm.constant;

import lombok.Getter;

/**
 * @author hmb
 */

@Getter
public enum ClockType {

    /**
     * 打卡类型
     */
    GO_TO(1,"上班"),
    GET_OFF(2,"下班");

    ClockType(int value, String name) {
        this.value = value;
        this.name = name;
    }



    private int value;
    private String name;

    public static String valueOfName(Integer value){
        for (ClockType clockType : values()) {
            if (clockType.value == value){
                return clockType.name;
            }
        }
        return null;
    }
}
