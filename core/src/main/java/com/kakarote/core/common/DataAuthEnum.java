package com.kakarote.core.common;

/**
 * 数据权限枚举
 *
 * @author hmb
 */

public enum DataAuthEnum {
    /**
     *     数据权限 1、本人，2、本人及下属，3、本部门，4、本部门及下属部门，5、全部
     */
    MYSELF(1),
    MYSELF_AND_SUBORDINATE(2),
    THIS_DEPARTMENT(3),
    THIS_DEPARTMENT_AND_SUBORDINATE(4),
    ALL(5)
    ;


    DataAuthEnum(Integer value) {
        this.value = value;
    }

    private Integer value;

    public Integer getValue() {
        return value;
    }

    public static DataAuthEnum valueOf(Integer value){
        for (DataAuthEnum dataAuthEnum : values()) {
            if (dataAuthEnum.getValue().equals(value)){
                return dataAuthEnum;
            }
        }
        return MYSELF;
    }

}
