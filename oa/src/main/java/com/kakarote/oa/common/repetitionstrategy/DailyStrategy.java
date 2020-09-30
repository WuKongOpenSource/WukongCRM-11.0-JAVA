package com.kakarote.oa.common.repetitionstrategy;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.kakarote.oa.entity.BO.OaEventDTO;
import com.kakarote.oa.entity.PO.OaEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 每天重复策略
 * @author hmb
 */
@Component("Daily")
public class DailyStrategy extends AbstractRepetitionStrategy {

    @Override
    public List<OaEventDTO> processQuery(List<OaEvent> oaEventList, long startTime, long endTime) {
        List<OaEventDTO> eventList = new ArrayList<>();
        for (OaEvent oaEvent : oaEventList) {
            Date startDate = oaEvent.getRepeatStartTime();
            Date endDate = oaEvent.getRepeatEndTime();
            Integer repeatRate = oaEvent.getRepeatRate();
            //计算出日程的时间差
            int betweenDay = (int) DateUtil.betweenDay(oaEvent.getStartTime(), oaEvent.getEndTime(), true);
            //前端传的开始时间向前推移相应的时间差  好计算前几天的日程
            long startTime1 = startTime - DAY_TIME * betweenDay;
            //计算开始次数和结束次数
            int betweenStartDay = (int) DateUtil.between( startDate,new Date(startTime1), DateUnit.DAY,false);
            int betweenEndDay = (int) DateUtil.between( startDate,new Date(endTime), DateUnit.DAY,false);
            if (betweenEndDay < 0){
                continue;
            }
            //开始时间大于前端传来的开始时间  次数就从零开始
            if (betweenStartDay < 0){
                betweenStartDay = 0;
            }
            //计算次数（当前时间-日程开始时间）/循环频率 + 1 =次数
            int startCount = betweenStartDay / repeatRate;
            int endCount = betweenEndDay / repeatRate + 1;
            for (int i = startCount; i < endCount; i++) {
                int offset = repeatRate * i;
                DateTime offStartTime = DateUtil.offsetDay(oaEvent.getStartTime(), offset);
                DateTime offEndTime = DateUtil.offsetDay(oaEvent.getEndTime(), offset);
                if (offEndTime.getTime() < startTime){
                    continue;
                }
                if (isContinue(oaEvent,offStartTime,endDate)){
                    continue;
                }
                OaEventDTO oaEventDTO = transfer(oaEvent);
                oaEventDTO.setStartTime(offStartTime.getTime());
                oaEventDTO.setEndTime(offEndTime.getTime());
                eventList.add(oaEventDTO);
            }
        }
        return eventList;
    }

    @Override
    protected Date processCountTime(Date startTime, int offset) {
        return DateUtil.offsetDay(startTime,offset);
    }


}
