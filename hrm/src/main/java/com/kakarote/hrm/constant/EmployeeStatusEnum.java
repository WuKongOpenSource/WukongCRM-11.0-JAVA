package com.kakarote.hrm.constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum EmployeeStatusEnum {
    /**
     * 员工状态
     */
    OFFICIAL(1, 1, "正式"), TRY_OUT(1, 2, "试用"),
    INTERNSHIP(2, 3, "实习"), PART_TIME(2,4,"兼职"),
    SERVICE(2, 5, "劳务"), CONSULTANT(2, 6, "顾问"),
    RETURN(2, 7, "返聘"), OUTSOURCING(2, 8, "外包"),
    ;

    EmployeeStatusEnum(int type, int value, String name) {
        this.type = type;
        this.value = value;
        this.name = name;
    }

    private int type;
    private int value;
    private String name;

    public static String parseName(int type) {
        for (EmployeeStatusEnum value : EmployeeStatusEnum.values()) {
            if (value.value == type) {
                return value.name;
            }
        }
        return "";
    }

    public static int valueOfType(String name) {
        for (EmployeeStatusEnum value : EmployeeStatusEnum.values()) {
            if (value.name.equals(name)) {
                return value.value;
            }
        }
        return -1;
    }

    public static List<Map<String, Object>> parseMapByType(int type) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (EmployeeStatusEnum value : EmployeeStatusEnum.values()) {
            if (type == value.type) {
                Map<String, Object> map = new HashMap<>();
                map.put("name", value.name);
                map.put("value", value.value);
                mapList.add(map);
            }
        }
        return mapList;
    }

    public static List<Map<String, Object>> parseMap() {
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (EmployeeStatusEnum value : EmployeeStatusEnum.values()) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", value.name);
            map.put("value", value.value);
            mapList.add(map);
        }
        return mapList;
    }

    public int getType() {
        return type;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
