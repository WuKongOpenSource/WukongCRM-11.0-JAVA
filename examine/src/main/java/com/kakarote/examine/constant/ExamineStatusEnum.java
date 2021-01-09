package com.kakarote.examine.constant;

import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.exception.CrmException;

/**
 * 审核状态枚举 0待审核、1通过、2拒绝、3审核中 4:撤回 5 未提交 6 创建 7 已删除 8 作废
 */
public enum ExamineStatusEnum {
    AWAIT(0),
    PASS(1),
    REJECT(2),
    UNDERWAY(3),
    RECHECK(4),
    UN_SUBMITTED(5),
    CREATE(6),
    REMOVE(7),
    INVALID(8),
    ;

    private ExamineStatusEnum(Integer status) {
        this.status = status;
    }

    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public static ExamineStatusEnum valueOf(Integer status) {
        for (ExamineStatusEnum examineStatusEnum : values()) {
            if (examineStatusEnum.getStatus().equals(status)) {
                return examineStatusEnum;
            }
        }
        throw new CrmException(SystemCodeEnum.SYSTEM_NO_VALID);
    }
}
