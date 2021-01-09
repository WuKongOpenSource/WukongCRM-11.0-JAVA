package com.kakarote.admin.common;

/**
 * @author zhangzhiwei
 * 模块的枚举
 */

public enum AdminModuleEnum {

    /**
     * 任务审批
     */
    TASK_EXAMINE("taskExamine"),
    /**
     * crm模块
     */
    CRM("crm"),
    /**
     * 项目管理
     */
    PROJECT("project"),
    /**
     * 日志模块
     */
    LOG("log"),
    /**
     * 通讯录模块
     */
    BOOK("book"),
    /**
     * 办公模块
     */
    OA("oa"),
    /**
     * 商业智能模块
     */
    BI("bi"),
    /**
     * 邮箱模块
     */
    EMAIL("email"),
    /**
     * 日历模块
     */
    CALENDAR("calendar"),
    /**
     * 知识库
     */
    KNOWLEDGE("knowledge"),
    /**
     * 呼叫中心
     */
    CALL("call"),

    HRM("hrm"),

    JXC("jxc");

    private AdminModuleEnum(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return this.value;
    }

    public static String[] getValues() {
        String[] values = new String[values().length];
        for (int i = 0; i < values().length; i++) {
            values[i] = values()[i].getValue();
        }
        return values;
    }

    public static AdminModuleEnum parse(String module) {
        for (AdminModuleEnum adminModuleEnum : AdminModuleEnum.values()){
            if (adminModuleEnum.getValue().equals(module)){
                return adminModuleEnum;
            }
        }
        return null;
    }

}
