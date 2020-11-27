package com.kakarote.oa.common;

import com.kakarote.core.common.ResultCode;

public enum OaCodeEnum implements ResultCode {

    /**
     * 列表
     * */
    OA_CODE_ENUM(500,""),
    EVENT_ALREADY_DELETE(501,"日程已删除"),
    EXAMINE_END_TIME_IS_EARLIER_THAN_START_TIME(502,"审批结束时间早于开始时间"),
    TRAVEL_END_TIME_IS_EARLIER_THAN_START_TIME(503,"差旅结束时间早于开始时间"),
    THE_APPROVAL_FLOW_HAS_BEEN_DISABLED_OR_DELETED(504,"该审批流已被停用或删除"),
    DURATION_MUST_BE_THREE_DIGITS(505,"时长必须是三位数"),
    CURRENT_USER_DOES_NOT_HAVE_APPROVAL_AUTHORITY(506,"当前用户没有审批权限"),
    THE_NAME_OF_THE_CUSTOM_FORM_CANNOT_BE_REPEATED(507,"自定义表单名称不能重复"),
    SYSTEM_EXAMINE_CAN_NOT_MODIFY(508,"系统审批类型暂不支持编辑"),
    LOG_ALREADY_DELETE(509,"日志已删除"),
    ANNOUNCEMENT_ALREADY_DELETE(510,"公告已删除"),
    EXAMINE_ALREADY_DELETE(511,"审批已删除"),
    TOTAL_REIMBURSEMENT_ERROR(512,"请完善明细！"),
    TOTAL_AMOUNT_OF_EXPENSE_DETAILS_ERROR(513,"报销费用明细(s%)合计应大于零")
    ;

    OaCodeEnum(int code, String msg) {
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
