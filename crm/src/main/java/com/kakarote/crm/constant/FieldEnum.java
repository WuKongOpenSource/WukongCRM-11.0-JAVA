package com.kakarote.crm.constant;

import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.exception.CrmException;

/**
 * @author zhangzhiwei
 * 字段类型枚举
 */

public enum FieldEnum {
    /**
     * 单行文本
     */
    TEXT(1, "text"),
    /**
     * 多行文本
     */
    TEXTAREA(2, "textarea"),
    /**
     * 单选
     */
    SELECT(3, "select"),
    /**
     * 日期
     */
    DATE(4, "date"),
    /**
     * 数字
     */
    NUMBER(5, "number"),
    /**
     * 小数
     */
    FLOATNUMBER(6, "floatnumber"),
    /**
     * 手机
     */
    MOBILE(7, "mobile"),
    /**
     * 文件
     */
    FILE(8, "file"),
    /**
     * 多选
     */
    CHECKBOX(9, "checkbox"),
    /**
     * 人员
     */
    USER(10, "user"),
    /**
     * 部门
     */
    STRUCTURE(12, "structure"),
    /**
     * 日期时间
     */
    DATETIME(13, "datetime"),
    /**
     * 邮件
     */
    EMAIL(14, "email"),
    /**
     * 客户
     */
    CUSTOMER(15, "customer"),
    /**
     * 商机
     */
    BUSINESS(16, "business"),
    /**
     * 联系人
     */
    CONTACTS(17, "contacts"),
    /**
     * 地图
     */
    MAP_ADDRESS(18, "map_address"),
    /**
     * 产品类型
     */
    CATEGORY(19, "category"),
    /**
     * 合同
     */
    CONTRACT(20, "contract"),
    /**
     * 回款计划
     */
    RECEIVABLES_PLAN(21, "receivables_plan"),
    /**
     *
     */
    BUSINESS_CAUSE(22, "business_cause"),
    EXAMINE_CAUSE(23, "examine_cause"),
    ADDRESS(24, "address"),
    WEBSITE(25, "website"),
    RETURN_VISIT(26, "return_visit"),
    RETURN_VISIT_CONTRACT(27, "return_visit_contract"),
    SINGLE_USER(28, "single_user");

    FieldEnum(int type, String formType) {
        this.type = type;
        this.formType = formType;
    }

    private Integer type;
    private String formType;

    public static FieldEnum parse(Integer type) {
        for (FieldEnum fieldTypeEnum : FieldEnum.values()) {
            if (fieldTypeEnum.getType().equals(type)) {
                return fieldTypeEnum;
            }
        }
        throw new CrmException(SystemCodeEnum.SYSTEM_NO_VALID);
    }

    public static FieldEnum parse(String formType) {
        for (FieldEnum fieldTypeEnum : FieldEnum.values()) {
            if (fieldTypeEnum.getFormType().equals(formType)) {
                return fieldTypeEnum;
            }
        }
        return TEXT;
    }

    public Integer getType() {
        return type;
    }

    public String getFormType() {
        return formType;
    }
}
