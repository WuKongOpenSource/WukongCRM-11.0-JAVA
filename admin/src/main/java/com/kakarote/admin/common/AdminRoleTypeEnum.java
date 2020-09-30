package com.kakarote.admin.common;

/**
 * 角色类型枚举
 *
 * @author zhangzhiwei
 */
public enum AdminRoleTypeEnum {
    /**
     * 自定义
     */
    CUSTOM(0),
    MANAGER(1),
    CUSTOMER_MANAGER(2),
    PERSONNEL(3),
    FINANCE(4),
    WORK(5),
    OA(7),
    PROJECT(8),
    HRM(9),
    JXC(10)
    ;

    AdminRoleTypeEnum(Integer type) {
        this.type = type;
    }

    private Integer type;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public static AdminRoleTypeEnum parse(Integer type) {
        for (AdminRoleTypeEnum typeEnum : values()) {
            if (typeEnum.getType().equals(type)) {
                return typeEnum;
            }
        }
        return null;
    }

}
