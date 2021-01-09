package com.kakarote.examine.constant;

import cn.hutool.core.collection.ListUtil;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.exception.CrmException;

import java.util.Arrays;
import java.util.Objects;

/**
 * 审批label枚举 1 合同 2 回款 3发票 4薪资 5 采购审核 6采购退货审核 7销售审核 8 销售退货审核 9付款单审核10 回款单审核11盘点审核12调拨审核
 */
public enum ExamineEnum {
    CRM_CONTRACT(1, "合同"),
    CRM_RECEIVABLES(2, "回款"),
    CRM_INVOICE(3,"发票"),

    ;

    private ExamineEnum(Integer type, String remark) {
        this.type = type;
        this.remark = remark;
    }

    private Integer type;
    private String remark;

    public Integer getType() {
        return type;
    }

    public String getRemark() {
        return remark;
    }

    public static ExamineEnum valueOf(Integer type) {
        for (ExamineEnum examineEnum : values()) {
            if (examineEnum.getType().equals(type)) {
                return examineEnum;
            }
        }
        throw new CrmException(SystemCodeEnum.SYSTEM_NO_VALID);
    }

    public static ExamineModuleTypeEnum parseModule(Integer label) {
        if (Arrays.asList(1, 2, 3).contains(label)) {
            return ExamineModuleTypeEnum.Crm;
        }
        if (ListUtil.toList(4).contains(label)) {
            return ExamineModuleTypeEnum.Hrm;
        }
        if (Arrays.asList(5, 6, 7, 8, 9, 10, 11, 12).contains(label)) {
            return ExamineModuleTypeEnum.Jxc;
        }
        if (Objects.equals(0, label)) {
            return ExamineModuleTypeEnum.Oa;
        }
        throw new CrmException(SystemCodeEnum.SYSTEM_NO_VALID);
    }
}
