package com.kakarote.hrm.constant;

public enum QuitTypeEnum {
    /**
     * 离职类型
     */
    INITIATIVE(1, "主动离职"), PASSIVE(2, "被动离职");

    QuitTypeEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }


    private String name;
    private int value;

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }
    public static String  parseName(int type){
        for(QuitTypeEnum value : QuitTypeEnum.values()){
            if(value.value == type){
                return value.name;
            }
        }
        return "";
    }

}

