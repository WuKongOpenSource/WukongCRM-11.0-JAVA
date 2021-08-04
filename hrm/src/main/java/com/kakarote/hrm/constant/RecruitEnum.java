package com.kakarote.hrm.constant;

/**
 * 招聘枚举类
 */
public class   RecruitEnum {

    public enum RecruitPostWorkTime {
        /**
         * 职位工作期限
         */
        UNLIMITED(1,"不限"),WITHIN_A_YEAR(2,"1年以下"),ONE_TO_THREE_YEAR(3,"1-3年"),
        THREE_TO_FIVE_YEAR(4,"3-5年"),FIVE_TO_TEN_YEAR(5,"5-10年"),MORE_THAN_TEN_YEAR(6,"10年以上");

        RecruitPostWorkTime(int value, String name) {
            this.value = value;
            this.name = name;
        }


        private String name;
        private int value;

        public static String  parseName(int type){
            for(RecruitPostWorkTime value : RecruitPostWorkTime.values()){
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
    public enum RecruitPostEmergencyLevel{
        /**
         * 职位紧急程度
         */
        URGENT(1,"紧急"),GENERAL(2,"一般");

        RecruitPostEmergencyLevel(int value, String name) {
            this.value = value;
            this.name = name;
        }


        private String name;
        private int value;

        public static String  parseName(int type){
            for(RecruitPostEmergencyLevel value : RecruitPostEmergencyLevel.values()){
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

    //面试情况 1面试未完成 2面试通过 3面试未通过 4 面试取消

    public enum RecruitInterviewResult{
        /**
         * 职位紧急程度
         */
        UNFINISHED(1,"面试未完成"),PASS(2,"面试通过"),NOT_PASS(3,"面试未通过"),CANCEL(4,"面试取消");

        RecruitInterviewResult(int value, String name) {
            this.value = value;
            this.name = name;
        }


        private String name;
        private int value;

        public static String  parseName(int type){
            for(RecruitInterviewResult value : RecruitInterviewResult.values()){
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
}
