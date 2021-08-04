package com.kakarote.hrm.constant;

public enum ChangeReasonEnum {
    /**
     * 异动原因 1 组织架构调整 2个人申请 3 工作安排 4 违规违纪 5 绩效不达标 6 个人身体原因 7 不适应当前岗位
     */
    ORGANIZATIONAL_STRUCTURE_ADJUSTMENT(1,"组织架构调整"),INDIVIDUAL_APPLICATION(2,"个人申请"),WORKING_ARRANGEMENT(3,"工作安排"),
    VIOLATION_OF_RULES(4,"违规违纪"),NOT_TO_STANDARD(5,"绩效不达标"),HEALTH_ISSUES(6,"个人身体原因"),NOT_SUITABLE(7,"不适应当前岗位")
    ;

    ChangeReasonEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }


    private String name;
    private int value;

    public static String  parseName(int type){
        for(ChangeReasonEnum value : ChangeReasonEnum.values()){
            if(value.value == type){
                return value.name;
            }
        }
        return "";
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }
}
