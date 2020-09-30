package com.kakarote.core.feign.admin.entity;

/**
 * @author zhangzhiwei
 * 数据权限枚举
 */

public enum DataTypeEnum {
    /**
     * 数据权限
     */
    SELF(1),
    SELF_AND_CHILD(2),
    DEPT(3),
    DEPT_AND_CHILD(4),
    ALL(5);

    DataTypeEnum(Integer type) {
        this.type = type;
    }

    private Integer type;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public static DataTypeEnum parse(Integer type) {
        for (DataTypeEnum typeEnum : values()) {
            if (typeEnum.getType().equals(type)) {
                return typeEnum;
            }
        }
        return null;
    }
}
