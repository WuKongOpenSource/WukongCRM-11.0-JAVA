package com.kakarote.core.common;

import cn.hutool.core.util.StrUtil;

import java.util.Objects;

/**
 * 进销存的模块枚举
 *
 * @author zhangzhiwei
 * @date 2021年4月1日17:17:09
 */
public enum JxcEnum {

    PRODUCT(1, "产品"),
    SUPPLIER(2, "供应商"),
    PURCHASE(3, "采购订单"),
    RETREAT(4, "采购退货单"),
    SALE(5, "销售订单"),
    SALE_RETURN(6, "销售退货单"),
    RECEIPT(7, "入库单"),
    OUTBOUND(8, "出库单"),
    PAYMENT(9, "付款单"),
    COLLECTION(10, "回款单"),
    INVENTORY(11, "盘点"),
    ALLOCATION(12, "调拨"),
    DETAILED(13, "出入库明细"),
	INVENTORY_RECEIPT(14, "盘点入库"),
    NULL(0, "NULL");

    private final int type;

    private final String remarks;

    JxcEnum(int type, String remarks) {
        this.type = type;
        this.remarks = remarks;
    }

    public static JxcEnum parse(Integer type) {
        for (JxcEnum jxcEnum : JxcEnum.values()) {
            if (Objects.equals(jxcEnum.getType(),type)) {
                return jxcEnum;
            }
        }
        return NULL;
    }

    public int getType() {
        return type;
    }

    public String getRemarks() {
        return remarks;
    }

	public String getIndex() {
		return "jxc_" + name().toLowerCase();
	}

    public String getTable() {
        return name().toLowerCase();
    }

    public String getTableId(){
    	return StrUtil.toCamelCase(name().toLowerCase()) + "Id";
	}
}
