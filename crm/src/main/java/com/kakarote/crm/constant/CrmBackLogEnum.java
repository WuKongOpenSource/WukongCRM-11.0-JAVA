package com.kakarote.crm.constant;

import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.exception.CrmException;

/**
 * @author zhangzhiwei
 * 待办事项枚举
 */

public enum CrmBackLogEnum {
    /**
     * 待办事项模块 1今日需联系客户 2分配给我的线索 3分配给我的客户 4待进入公海的客户
     * 5待审核合同 6待审核回款 7待回款提醒 8即将到期的合同 9待回访合同 10待审核发票
     */
    TODAY_CUSTOMER(1),
    FOLLOW_LEADS(2),
    FOLLOW_CUSTOMER(3),
    TO_ENTER_CUSTOMER_POOL(4),
    CHECK_CONTRACT(5),
    CHECK_RECEIVABLES(6),
    REMIND_RECEIVABLES_PLAN(7),
    END_CONTRACT(8),
    REMIND_RETURN_VISIT_CONTRACT(9),
    CHECK_INVOICE(10),
    TODAY_LEADS(11),
    TODAY_BUSINESS(12),
    ;

    CrmBackLogEnum(Integer type) {
        this.type = type;
    }

    private Integer type;

    public Integer getType() {
        return type;
    }

    public static CrmBackLogEnum parse(int type) {
        for (CrmBackLogEnum crmBackLogEnum : CrmBackLogEnum.values()) {
            if (crmBackLogEnum.getType() == type) {
                return crmBackLogEnum;
            }
        }
        throw new CrmException(SystemCodeEnum.SYSTEM_NO_VALID);
    }
}
