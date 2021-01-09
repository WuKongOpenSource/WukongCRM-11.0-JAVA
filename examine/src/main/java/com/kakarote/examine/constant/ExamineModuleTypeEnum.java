package com.kakarote.examine.constant;

/**
 * 审批类型枚举
 */
public enum ExamineModuleTypeEnum {
    Crm,
    Jxc,
    Hrm,
    Oa,
    ;

    public String getServerName() {
        return "examine" + name() + "Service";
    }

}
