package com.kakarote.hrm.constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum CandidateStatusEnum {
    /**
     * 候选人状态 1 新候选人 2初选通过 3安排面试 4面试通过 5已发offer 6待入职 7已淘汰 8已入职
     */
    NEW_CANDIDATE(1, "新候选人"), PRIMARY_ELECTION(2, "初选通过"),
    ARRANGE_AN_INTERVIEW(3, "安排面试"), PASS_THE_INTERVIEW(4,"面试通过"),
    OFFER_HAS_BEEN_SENT(5, "已发offer"), PENDING_ENTRY(6, "待入职"),
    OBSOLETE( 7, "已淘汰"), HAVE_JOINED(8, "已入职"),CANCEL_INTERVIEW(9,"面试取消");

    CandidateStatusEnum( int value, String name) {
        this.value = value;
        this.name = name;
    }

    private int value;
    private String name;

    public static String parseName(int type) {
        for (CandidateStatusEnum value : CandidateStatusEnum.values()) {
            if (value.value == type) {
                return value.name;
            }
        }
        return "";
    }

    public static int valueOfType(String name) {
        for (CandidateStatusEnum value : CandidateStatusEnum.values()) {
            if (value.name.equals(name)) {
                return value.value;
            }
        }
        return -1;
    }


    public static List<Map<String, Object>> parseMap() {
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (CandidateStatusEnum value : CandidateStatusEnum.values()) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", value.name);
            map.put("value", value.value);
            mapList.add(map);
        }
        return mapList;
    }


    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
