package com.kakarote.hrm.constant;


/**
 * 用户考勤组变更记录类型
 * @author hmb
 * 记录类型 1用户变更考勤组 2考勤组更新 3班次更新 4打卡方式更新
 */
public interface AttendanceRecordType{

    Integer USER = 1;

    Integer ATTENDANCE = 2;

    Integer SHIFT = 3;

    Integer CLOCK_TYPE = 4;

}
