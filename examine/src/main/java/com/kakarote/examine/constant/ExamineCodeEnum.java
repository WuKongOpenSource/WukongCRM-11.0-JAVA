package com.kakarote.examine.constant;

import com.kakarote.core.common.ResultCode;

/**
 * 审批错误枚举
 * 4200-4300
 */
public enum ExamineCodeEnum implements ResultCode {

    /**
     * 列表
     * */
    EXAMINE_START_ERROR(4200,"只能拥有一个启用的审批流程"),
    EXAMINE_RECHECK_PASS_ERROR(4201, "该审核已通过，不能撤回"),
    EXAMINE_AUTH_ERROR(4202, "该审核已通过，不能撤回"),
    EXAMINE_ROLE_NO_USER_ERROR(4203, "自选的角色下没有检测到人员，请核实！"),
    EXAMINE_NAME_NO_USER_ERROR(4204, "审批流名称重复，请核实！"),
    EXAMINE_SPECIAL_TYPE_NOT_DELETE_ERROR(4204, "系统默认审批不允许删除！"),
    ;

    ExamineCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private int code;
    private String msg;

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
