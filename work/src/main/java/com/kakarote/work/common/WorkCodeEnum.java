package com.kakarote.work.common;

import com.kakarote.core.common.ResultCode;

public enum WorkCodeEnum implements ResultCode {
    WORK_CREATE_USER_EXIT_ERROR(3001,"项目创建人不可以退出"),
    WORK_EXIST_ERROR(3002,"项目不存在"),
    WORK_USER_EXIST_ERROR(3003,"参与人不存在"),
    WORK_TASK_LABEL_EXIST_ERROR(3004,"任务没有使用该标签"),
    WORK_TASK_DELETE_ERROR(3005,"任务已被删除"),
    WORK_TASK_EXIST_ERROR(3006,"任务不存在"),
    WORK_AUTH_ERROR(3007,"无权访问"),
    WORK_LABEL_DELETE_ERROR(3008,"使用中的标签不能删除"),
    WORK_TASK_IN_TRASH_ERROR(3009,"任务不在回收站")
    ;

    WorkCodeEnum(int code, String msg) {
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
