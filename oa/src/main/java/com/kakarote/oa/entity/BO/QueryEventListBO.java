package com.kakarote.oa.entity.BO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class QueryEventListBO {
    private Long startTime;
    private Long endTime;
    private List<Integer> typeIds;
    private Long userId;
}
