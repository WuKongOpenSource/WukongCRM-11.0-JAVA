package com.kakarote.examine.constant;

import cn.hutool.core.util.StrUtil;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.exception.CrmException;

import java.util.Objects;

/**
 * 审批类型枚举
 *
 * @date 2020年11月17日
 */
public enum ExamineTypeEnum {

    /*
      条件审批
     */
    CONDITION(0),
    /**
     * 指定成员审批
     */
    MEMBER(1),
    /**
     * 负责人主管审批
     */
    SUPERIOR(2),
    /**
     * 指定角色审批
     */
    ROLE(3),
    /**
     * 发起人自选成员审批
     */
    OPTIONAL(4),
    /**
     * 连续多级主管审批
     */
    CONTINUOUS_SUPERIOR(5),

    /**
     * 管理员审批，只在找不到审批对象时存在
     */
    MANAGER(6);

    private ExamineTypeEnum(Integer type) {
        this.type = type;
    }

    private Integer type;

    public Integer getType() {
        return type;
    }

    public static ExamineTypeEnum valueOf(Integer type) {
        for (ExamineTypeEnum examineTypeEnum : values()) {
            if (Objects.equals(type, examineTypeEnum.getType())) {
                return examineTypeEnum;
            }
        }
        throw new CrmException(SystemCodeEnum.SYSTEM_NO_VALID);
    }

    public String getServerName() {
        return StrUtil.toCamelCase(name().toLowerCase()) + "Service";
    }
}
