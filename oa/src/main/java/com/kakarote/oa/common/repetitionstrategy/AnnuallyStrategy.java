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
 * 每年重复策略
 *
 * @author hmb
 */
@Component("Annually")
public class  AnnuallyStrategy extends AbstractRepetitionStrategy {


    @Override
    public List<OaEventDTO> processQuery(List<OaEvent> oaEventList, long startTime, long endTime) {
        List<OaEventDTO> eventList = new ArrayList<>();
        //创建一个日期范围list
        List<DateTime> dateTimes = DateUtil.rangeToList(new Date(startTime), new Date(endTime), DateField.DAY_OF_YEAR);
        for (DateTime day : dateTimes) {
            for (OaEvent oaEvent : oaEventList) {
                Date endDate = oaEvent.getRepeatEndTime();
                DateTime offStartTime = DateUtil.parseDateTime(day.year()+"-"+ DateUtil.format(oaEvent.getStartTime(),"MM-dd HH:mm:ss"));
                if (isContinue(oaEvent, offStartTime, endDate)) {
                    continue;
                }
                //日程开始时间大于当前天就跳过
                if (oaEvent.getStartTime().getTime() > day.getTime()){
                    continue;
                }
                long betweenYear = DateUtil.betweenYear(oaEvent.getStartTime(),offStartTime, true);
                Integer repeatRate = oaEvent.getRepeatRate();
                //余数不等于0修跳过
                if (betweenYear % repeatRate != 0) {
                    continue;
                }
                //不是同一天就跳过
                if (!DateUtil.isSameDay(day,offStartTime)){
                    continue;
                }
                long duration = oaEvent.getEndTime().getTime() - oaEvent.getStartTime().getTime();
                OaEventDTO oaEventDTO = transfer(oaEvent);
                oaEventDTO.setStartTime(offStartTime.getTime());
                oaEventDTO.setEndTime(offStartTime.getTime() + duration);
                eventList.add(oaEventDTO);
            }
        }
        return eventList;
    }

    @Override
    protected Date processCountTime(Date startTime, int offset) {
        return DateUtil.offset(startTime, DateField.YEAR, offset);
    }


}
