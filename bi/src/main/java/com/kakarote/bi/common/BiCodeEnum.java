package com.kakarote.bi.common;

import com.kakarote.core.common.ResultCode;

/**
 * @author zhangzhiwei
 * 管理后台响应错误代码枚举类
 */

public enum BiCodeEnum implements ResultCode {
    //客户模块管理
    BI_DATE_PARSE_ERROR(3101, "日期解析错误！")
    ;

    BiCodeEnum(int code, String msg) {
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
