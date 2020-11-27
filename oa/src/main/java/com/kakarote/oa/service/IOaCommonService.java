package com.kakarote.oa.service;

/**
 * @author JiaS
 * @date 2020/11/13
 */
public interface IOaCommonService {

    /**
     * 初始化oa数据
     * @return boolean
     */
    boolean initOaData();

    /**
     * 初始化日历数据
     * @return boolean
     */
    boolean initCalendarData();

    /**
     * 初始化oa审批数据
     * @return boolean
     */
    boolean initOaExamineData();
}
