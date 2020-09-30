package com.kakarote.oa.common.repetitionstrategy;

import com.kakarote.oa.entity.BO.OaEventDTO;
import com.kakarote.oa.entity.PO.OaEvent;

import java.util.List;
import java.util.Map;

/**
 * 日程重复策略接口
 * @author hmb
 */
public interface RepetitionStrategy {

    /**
     * 执行查询
     * @param oaEventList 日程列表
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    List<OaEventDTO> processQuery(List<OaEvent> oaEventList, long startTime, long endTime);

    /**
     * 设置特殊日期
     * @param specialDayMap 特殊日期
     */
    void setSpecialDayMap(Map<String, Integer> specialDayMap);


    /**
     *  处理保存的开始结束时间
     * @param oaEvent 日程对象
     * @return
     */
    OaEvent processTime(OaEvent oaEvent);
}
