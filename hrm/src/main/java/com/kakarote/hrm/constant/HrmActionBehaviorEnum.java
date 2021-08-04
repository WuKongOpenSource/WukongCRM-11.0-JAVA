package com.kakarote.hrm.constant;

public enum HrmActionBehaviorEnum {

    /**
     * 操作行为 1 新建 2 编辑 3 删除 4 转正 5 调岗 6 晋升 7 降级 8 转全职员工 9 离职 10 参保方案
     */
    ADD(1,"新建"),UPDATE(2,"编辑"),DELETE(3,"删除"),
    BECOME(4,"转正"),
    CHANGE_POST(5,"调岗"),PROMOTED(6,"晋升"),DEGRADE(7,"降级"),
    CHANGE_FULL_TIME_EMPLOYEE(8,"转为全职员工"),QUIT(9,"离职"),
    PARTICIPATION_PLAN(10,"参保方案");


    HrmActionBehaviorEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }


    private String name;
    private int value;

    public static String  parseName(int type){
        for(HrmActionBehaviorEnum value : HrmActionBehaviorEnum.values()){
            if(value.value == type){
                return value.name;
            }
        }
        return "";
    }

    public static int  valueOfType(String name){
        for(HrmActionBehaviorEnum value : HrmActionBehaviorEnum.values()){
            if(value.name.equals(name)){
                return value.value;
            }
        }
        return -1;
    }

    public static HrmActionBehaviorEnum  parse(int type){
        for(HrmActionBehaviorEnum value : HrmActionBehaviorEnum.values()){
            if(value.value == type){
                return value;
            }
        }
        return BECOME;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }


}
