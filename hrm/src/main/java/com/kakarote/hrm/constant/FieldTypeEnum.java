package com.kakarote.hrm.constant;

public enum FieldTypeEnum {

    /**
     * 自定义字段类型
     * 字段类型  1 单行文本 2 多行文本 3 单选 4日期 5 数字 6 小数 7 手机  8 文件 9 多选   10 日期时间 11 邮箱 12 籍贯地区
     */
    TEXT(1,"单行文本"),
    TEXTAREA(2,"多行文本"),
    SELECT(3,"单选"),
    DATE(4,"日期"),
    NUMBER(5,"数字"),
    DECIMAL(6,"小数"),
    MOBILE(7,"手机"),
    FILE(8,"文件"),
    CHECKBOX(9,"多选"),
    DATETIME(13,"日期时间"),
    EMAIL(14,"邮箱"),
    AREA(40,"省市区"),
    ;

    FieldTypeEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static FieldTypeEnum  parse(int formType){
        for(FieldTypeEnum value : FieldTypeEnum.values()){
            if(value.value == formType){
                return value;
            }
        }
        return TEXT;
    }


    private String name;
    private int value;

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }
}
