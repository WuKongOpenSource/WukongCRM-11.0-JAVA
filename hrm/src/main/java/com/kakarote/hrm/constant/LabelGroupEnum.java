package com.kakarote.hrm.constant;

public enum LabelGroupEnum {

    /**
     *      * 1 员工个人信息 2 通讯信息 3 教育经历 4 工作经历 5 证书/证件 6 培训经历 7 联系人
     *      * 11 岗位信息 12 离职信息
     *      * 21 合同信息
     *      * 31 工资卡信息 32 社保信息
     */
    PERSONAL(1,"个人信息","员工"),
    COMMUNICATION(2,"通讯信息","通讯信息"),
    EDUCATIONAL_EXPERIENCE(3,"教育经历","教育经历"),
    WORK_EXPERIENCE(4,"工作经历","工作经历"),
    CERTIFICATE(5,"证书/证件","证书/证件"),
    TRAINING_EXPERIENCE(6,"培训经历","培训经历"),
    CONTACT_PERSON(7,"联系人","联系人"),
    POST(11,"岗位信息","岗位信息"),QUIT(12,"离职信息","离职信息"),
    CONTRACT(21,"合同信息","合同信息"),
    SALARY_CARD(31,"工资卡信息","工资卡信息"),
    SOCIAL_SECURITY(32,"社保信息","社保信息"),
    RECRUIT_POST(41,"招聘职位","职位"),
    RECRUIT_CANDIDATE(42,"候选人","候选人");


    LabelGroupEnum(int value, String name,String desc) {
        this.value = value;
        this.name = name;
        this.desc = desc;
    }

    //名称
    private String name;

    //label
    private int value;

    //备注
    private String desc;

    public static LabelGroupEnum parse(int label){
        for (LabelGroupEnum labelGroupEnum : values()) {
            if (label == labelGroupEnum.value){
                return labelGroupEnum;
            }
        }
        return PERSONAL;
    }


    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
