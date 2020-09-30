package com.kakarote.core.common;

/**
 * @author zhangzhiwei
 * 默认的状态枚举
 */

public enum BaseStatusEnum {
    //开启
    OPEN(1),
    //关闭
    CLOSE(0);

    BaseStatusEnum(Integer status) {
        this.status = status;
    }

    private Integer status;

    public Integer getStatus() {
        return status;
    }


}
