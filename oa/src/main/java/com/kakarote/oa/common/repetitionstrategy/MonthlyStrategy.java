package com.kakarote.oa.common.repetitionstrategy;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.kakarote.oa.entity.BO.OaEventDTO;
import com.kakarote.oa.entity.PO.OaEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 每月循环策略
 *
 * @author hmb
 */
@Component("Monthly")
public class MonthlyStrategy extends AbstractRepetitionStrategy {

    @Override
    public List<OaEventDTO> processQuery(List<OaEvent> oaEventList, long startTime, long endTime) {
        List<OaEventDTO> eventList = new ArrayList<>();
        List<DateTime> dateTimes = DateUtil.rangeToList(new Date(startTime), new Date(endTime), DateField.DAY_OF_YEAR);
        for (DateTime day : dateTimes) {
            for (OaEvent oaEvent : oaEventList) {
                Date endDate = oaEvent.getRepeatEndTime();
                long duration = oaEvent.getEndTime().getTime() - oaEvent.getStartTime().getTime();
                if (isContinue(oaEvent,day,endDate)){
                    continue;
                }
                if (oaEvent.getStartTime().getTime() > day.getTime()){
                    continue;
                }
                DateTime offStartTime = DateUtil.parseDateTime(DateUtil.formatDate(day)+" "+ DateUtil.formatTime(oaEvent.getStartTime()));
                long betweenMonth = DateUtil.betweenMonth(oaEvent.getStartTime(),offStartTime, true);
                Integer repeatRate = oaEvent.getRepeatRate();
                if (betweenMonth % repeatRate != 0) {
                    continue;
                }
                String repeatTime = oaEvent.getRepeatTime();
                int repeatDate = Integer.parseInt(repeatTime);
                final int dd = Integer.parseInt(DateUtil.format(day, "dd"));
                if (dd == repeatDate){
                    OaEventDTO oaEventDTO = transfer(oaEvent);
                    oaEventDTO.setStartTime(offStartTime.getTime());
                    oaEventDTO.setEndTime(offStartTime.getTime()+duration);
                    eventList.add(oaEventDTO);
                }
            }
        }
        return eventList;
    }

    @Override
    protected Date processCountTime(Date startTime, int offset) {
        return DateUtil.offsetMonth(startTime,offset);
    }
}
