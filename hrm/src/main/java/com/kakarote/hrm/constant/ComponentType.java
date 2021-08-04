package com.kakarote.hrm.constant;

public enum ComponentType {

    /**
     * 关联表类型 0 不需要关联 1 hrm员工 2 hrm部门 3 hrm职位 4 系统用户 5 系统部门 6 招聘渠道
     */
    NO(0,"不需要关联"),HRM_EMPLOYEE(1,"hrm员工"),HRM_DEPT(2,"hrm部门"),HRM_POST(3,"hrm职位"),
    ADMIN_USER(4,"系统用户"),ADMIN_DEPT(5,"系统部门"),RECRUIT_CHANNEL(6,"招聘渠道"),HOMETOWN(7,"籍贯地区");

    ComponentType(int value, String name) {
        this.value = value;
        this.name = name;
    }


    private String name;
    private int value;

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }
}
