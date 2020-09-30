package com.kakarote.oa.entity.BO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueryEventByIdBO {
    private Integer eventId;
    private Long startTime;
    private Long endTime;
}
