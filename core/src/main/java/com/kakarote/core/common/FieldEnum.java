package com.kakarote.core.common;

import lombok.Getter;

/**
 * @author JiaS
 * @date 2021/1/11
 */
public enum FieldEnum {

    /**
     *     字段类型 1 单行文本 2 多行文本 3 单选 4日期 5 数字 6 小数 7 手机  8 文件 9 多选 10 人员
     *     11 附件 12 部门 13 日期时间 14 邮箱 15客户 16 商机 17 联系人 18 地图 19 产品类型 20 合同 21 回款计划
     */
    TEXT(1, "text","单行文本"),
    TEXTAREA(2, "textarea","多行文本"),
    SELECT(3, "select","单选"),
    DATE(4, "date","日期"),
    NUMBER(5, "number","数字"),
    FLOATNUMBER(6, "floatnumber","小数"),
    MOBILE(7, "mobile","手机"),
    FILE(8, "file","文件"),
    CHECKBOX(9, "checkbox","多选"),
    USER(10, "user","人员"),
    ATTACHMENT(11,"attachment","附件"),
    STRUCTURE(12, "structure","部门"),
    DATETIME(13, "datetime","日期时间"),
    EMAIL(14, "email","邮件"),
    CUSTOMER(15, "customer","客户"),
    BUSINESS(16, "business","商机"),
    CONTACTS(17, "contacts","联系人"),
    MAP_ADDRESS(18, "map_address","地图"),
    CATEGORY(19, "category","产品类型"),
    CONTRACT(20, "contract","合同"),
    RECEIVABLES_PLAN(21, "receivables_plan","回款计划"),
    BUSINESS_CAUSE(22, "business_cause","商机业务"),
    EXAMINE_CAUSE(23, "examine_cause","审批业务"),
    ADDRESS(24, "address","地址"),
    WEBSITE(25, "website","网址"),
    RETURN_VISIT(26, "return_visit","回访"),
    RETURN_VISIT_CONTRACT(27, "return_visit_contract","回访合同"),
    SINGLE_USER(28, "single_user","单个人员"),
    /**
     * 进销存
     * */
    PIC(29, "pic","图片"),
    SUPPLIER_CAUSE(30, "supplier_cause","供应商"),
    PURCHASE_CAUSE(31, "purchase_cause","采购订单"),
    SALE_CAUSE(32, "sale_cause","销售订单"),
    WARRHOUSE_CAUSE(33, "warehouse_cause","仓库"),
    RELATED_ID(34,"related_id","关联对象"),
    COLLECTION_OBJECT(35,"collection_object","收藏"),
    STATE_CAUSE(39,"state_cause","状态标识"),
    /**
     * 人资
     * */
    AREA(40,"area","省市区"),

    BOOLEAN_VALUE(41,"boolean_value","布尔值"),
    PERCENT(42,"percent","百分数"),
    AREA_POSITION(43,"position","地址"),
    CURRENT_POSITION(44,"location","定位"),
    DETAIL_TABLE(45,"detail_table","明细表格"),
    HANDWRITING_SIGN(46,"handwriting_sign","手写签名"),
    DATE_INTERVAL(48,"date_interval","日期区间"),
    OPTIONS_TYPE(49,"options_type","选项字段:逻辑表单、批量编辑、其他"),
    DESC_TEXT(50,"desc_text","描述文字"),
    CALCULATION_FUNCTION(51,"calculation_function","计算函数"),
    RELATE_CAUSE(52,"relate_cause","关联业务"),
    QUOTE_TYPE(53,"quote_type","引用字段"),
    ;

    @Getter
    private Integer type;

    @Getter
    private String formType;

    @Getter
    private String desc;

    FieldEnum() {
    }

    FieldEnum(Integer type, String formType, String desc) {
        this.type = type;
        this.formType = formType;
        this.desc = desc;
    }


    public static FieldEnum parse(Integer type) {
        for (FieldEnum fieldTypeEnum : FieldEnum.values()) {
            if (fieldTypeEnum.getType().equals(type)) {
                return fieldTypeEnum;
            }
        }
        return TEXT;
    }

    public static FieldEnum parse(String formType) {
        for (FieldEnum fieldTypeEnum : FieldEnum.values()) {
            if (fieldTypeEnum.getFormType().equals(formType)) {
                return fieldTypeEnum;
            }
        }
        return TEXT;
    }
}
