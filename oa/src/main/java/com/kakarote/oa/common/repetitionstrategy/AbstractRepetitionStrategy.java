package com.kakarote.oa.common.repetitionstrategy;

import cn.hutool.core.date.DateUtil;
import com.kakarote.oa.entity.BO.OaEventDTO;
import com.kakarote.oa.entity.PO.OaEvent;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 日程重复策略抽象类(提供公共处理方法)
 * @author hmb
 */
public abstract class AbstractRepetitionStrategy implements RepetitionStrategy{


    private Map<String, Integer> specialDayMap;
    /**
     * 每天的毫秒数
     */
    static final long DAY_TIME = 3600 * 24 * 1000;


    /**
     * 处理不同重复类型的数据
     * @param oaEventList 处理数据
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    @Override
    public abstract List<OaEventDTO> processQuery(List<OaEvent> oaEventList, long startTime, long endTime);

    /**
     *  处理保存的开始结束时间
     * @param oaEvent 日程对象
     * @return
     */
    @Override
    public OaEvent processTime(OaEvent oaEvent){
        Date startTime = oaEvent.getStartTime();
        Integer repeatRate = oaEvent.getRepeatRate();
        Integer endType = oaEvent.getEndType();
        String endTypeConfig = oaEvent.getEndTypeConfig();
        Date repeatEndTime = null;
        if (endType == 2){
            Integer count = Integer.valueOf(endTypeConfig);
            int offset = repeatRate * count;
            repeatEndTime = processCountTime(startTime,offset);
        }else if (endType == 3){
            repeatEndTime = DateUtil.parseDateTime(endTypeConfig);
        }
        oaEvent.setRepeatStartTime(startTime);
        oaEvent.setRepeatEndTime(repeatEndTime);
        return oaEvent;
    }

    protected abstract Date processCountTime(Date startTime,int offset);


    boolean isContinue(OaEvent oaEvent, Date offStartTime, Date endDate) {
        String key = oaEvent.getEventId() + "_" + DateUtil.formatDate(offStartTime);
        if (specialDayMap.containsKey(key)) {
            Integer status = specialDayMap.get(key);
            if (status == 1 || status == 2) {
                return true;
            }
        }
        return endDate != null && offStartTime.getTime() >= endDate.getTime();
    }


    @Override
    public void setSpecialDayMap(Map<String, Integer> specialDayMap) {
        this.specialDayMap = specialDayMap;
    }

    OaEventDTO transfer(OaEvent oaEvent){
        OaEventDTO oaEventDTO = new OaEventDTO();
        oaEventDTO.setEventId(oaEvent.getEventId());
        oaEventDTO.setTitle(oaEvent.getTitle());
        oaEventDTO.setOwnerUserIds(oaEvent.getOwnerUserIds());
        oaEventDTO.setColor(oaEvent.getColor());
        oaEventDTO.setBatchId(oaEvent.getBatchId());
        oaEventDTO.setCreateUserId(oaEvent.getCreateUserId());
        oaEventDTO.setEventType(1);
        oaEventDTO.setTypeId(oaEvent.getTypeId());
        return oaEventDTO;
    }
}
