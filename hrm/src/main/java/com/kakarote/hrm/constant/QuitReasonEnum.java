package com.kakarote.hrm.constant;

public enum QuitReasonEnum {
    /**
     * 离职原因
     * 主动离职 1家庭原因 2身体原因 3薪资原因 4交通不便 5工作压力 6管理问题 7无晋升机会 8职业规划 9合同到期放弃续签 10其他个人原因
     * 被动离职 11试用期内辞退 12违反公司条例 13组织调整/裁员 14绩效不达标辞退 15合同到期不续签 16 其他原因被动离职
     */
    FAMILY(QuitTypeEnum.INITIATIVE,1,"家庭原因"),HEALTH(QuitTypeEnum.INITIATIVE,2,"身体原因"),SALARY(QuitTypeEnum.INITIATIVE,3,"薪资原因"),
    INCONVENIENT_TRAFFIC(QuitTypeEnum.INITIATIVE,4,"交通不便"),WORKING_PRESSURE(QuitTypeEnum.INITIATIVE,5,"工作压力"),MANAGEMENT_ISSUES(QuitTypeEnum.INITIATIVE,6,"管理问题"),
    NO_PROMOTION_OPPORTUNITIES(QuitTypeEnum.INITIATIVE,7,"无晋升机会"),CAREER_PLANNING(QuitTypeEnum.INITIATIVE,8,"职业规划"),GIVE_UP_RENEWAL(QuitTypeEnum.INITIATIVE,9,"合同到期放弃续签"),
    OTHER_PERSONAL_REASONS(QuitTypeEnum.INITIATIVE,10,"其他个人原因"),
    DISMISSAL_OF_TRIAL_PERIOD(QuitTypeEnum.PASSIVE,11,"使用期内被辞退"),VIOLATION_OF_COMPANY_REGULATIONS(QuitTypeEnum.PASSIVE,12,"违反公司条例"),LAYOFFS(QuitTypeEnum.PASSIVE,13,"组织调整/裁员"),
    UNDERPERFORMANCE(QuitTypeEnum.PASSIVE,14,"绩效不达标辞退"),THE_CONTRACT_EXPIRES_WITHOUT_RENEWAL(QuitTypeEnum.PASSIVE,15,"合同到期不续签"),OTHER_REASONS(QuitTypeEnum.PASSIVE,16,"其他原因")
    ;

    QuitReasonEnum(QuitTypeEnum quitType, int value, String name) {
        this.quitType = quitType;
        this.value = value;
        this.name = name;
    }

    private QuitTypeEnum quitType;
    private String name;
    private int value;

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public QuitTypeEnum getQuitType() {
        return quitType;
    }

    public static QuitReasonEnum  parse(int type){
        for(QuitReasonEnum value : QuitReasonEnum.values()){
            if(value.value == type){
                return value;
            }
        }
        return FAMILY;
    }

    public static String  parseName(int type){
        for(QuitReasonEnum value : QuitReasonEnum.values()){
            if(value.value == type){
                return value.name;
            }
        }
        return "";
    }
}
