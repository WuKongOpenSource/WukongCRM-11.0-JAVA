package com.kakarote.core.feign.crm.entity;

import com.kakarote.core.entity.PageEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueryEventCrmPageBO extends PageEntity {

    private Long userId;

    private Long time;

    private Integer type;

}
