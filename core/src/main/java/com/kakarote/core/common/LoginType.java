package com.kakarote.core.common;

import lombok.Getter;

/**
 * 登录方式
 * @author hmb
 */
@Getter
public enum LoginType {
    PASSWORD(1),
    SMS_CODE(2),
    CP_CODE(3),
    CP_QR_CODE(4),
    ;



    private Integer type;

    LoginType(Integer type) {
        this.type = type;
    }

    public static LoginType valueOf(Integer type){
        for (LoginType value : values()) {
            if (type.equals(value.getType())){
                return value;
            }
        }
        return PASSWORD;
    }
}
