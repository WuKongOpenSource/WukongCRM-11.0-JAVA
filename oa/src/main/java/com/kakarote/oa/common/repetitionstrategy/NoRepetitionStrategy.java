package com.kakarote.oa.common.repetitionstrategy;


import com.kakarote.oa.constart.RepetitionType;
import com.kakarote.oa.entity.BO.OaEventDTO;
import com.kakarote.oa.entity.PO.OaEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 从不重复策略
 */
@Component("NoRepetition")
public class NoRepetitionStrategy extends AbstractRepetitionStrategy {


    @Override
    public List<OaEventDTO> processQuery(List<OaEvent> oaEventList, long startTime, long endTime) {
        List<OaEventDTO> eventList = new ArrayList<>();
        for (OaEvent oaEvent : oaEventList) {
            Date startDate = oaEvent.getRepeatStartTime();
            Date endDate = oaEvent.getRepeatEndTime();
            OaEventDTO oaEventDTO = transfer(oaEvent);
            oaEventDTO.setStartTime(startDate.getTime());
            oaEventDTO.setEndTime(endDate.getTime());
            eventList.add(oaEventDTO);
        }
        return eventList;
    }

    @Override
    public OaEvent processTime(OaEvent oaEvent) {
        oaEvent.setRepetitionType(RepetitionType.NO_REPETITION.getType());
        oaEvent.setRepeatStartTime(oaEvent.getStartTime());
        oaEvent.setRepeatEndTime(oaEvent.getEndTime());
        return oaEvent;
    }

    @Override
    protected Date processCountTime(Date startTime, int offset) {
        return startTime;
    }
}
