package com.kakarote.examine.constant;

import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.exception.CrmException;

/**
 * 判断条件枚举
 * 连接条件 1 等于 2 大于 3 小于 4 大于等于 5 小于等于 6 两者之间 7 包含 8 员工/部门/角色 11完全等于
 */
public enum ConditionTypeEnum {

    EQ(1),
    GT(2),
    LT(3),
    GE(4),
    LE(5),
    AMONG(6),
    CONTAIN(7),
    PERSONNEL(8),
    EQUALS(11),
    ;

    private ConditionTypeEnum(Integer type) {
        this.type = type;
    }

    private Integer type;

    public Integer getType() {
        return type;
    }

    public static ConditionTypeEnum valueOf(Integer type) {
        for (ConditionTypeEnum conditionEnum : values()) {
            if (conditionEnum.getType().equals(type)) {
                return conditionEnum;
            }
        }
        throw new CrmException(SystemCodeEnum.SYSTEM_NO_VALID);
    }
}
