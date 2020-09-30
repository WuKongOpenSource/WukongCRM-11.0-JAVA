package com.kakarote.oa.constart;

import lombok.Getter;

@Getter
public enum  RepetitionType {
    /**
     * RepetitionType
     */
    NO_REPETITION( 1,"从不重复","NoRepetition"),
    DAILY ( 2,"每天" ,"Daily"),
    WEEKLY ( 3,"每周","Weekly"),
    MONTHLY ( 4 ,"每月","Monthly"),
    ANNUALLY ( 5,"每年" ,"Annually");

    private Integer type;
    private String remark;
    private String name;

    RepetitionType(Integer type, String remark,String name) {
        this.type = type;
        this.remark = remark;
        this.name = name;
    }

    public static RepetitionType valueOf(Integer type) {
        for (RepetitionType c : RepetitionType.values ()) {
            if ( c.getType ().equals ( type ) ) {
                return c;
            }
        }
        return RepetitionType.NO_REPETITION;
    }

}
