package com.kakarote.crm.entity.BO;

import com.kakarote.core.entity.PageEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrmCensusBO extends PageEntity {

    private Integer marketingId;

    private Integer status;
}
