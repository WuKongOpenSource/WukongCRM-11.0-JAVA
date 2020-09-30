package com.kakarote.oa.entity.BO;

import com.kakarote.oa.entity.PO.OaEvent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateEventBO {

    private Integer type;

    private Long time;

    private OaEvent event;
}
