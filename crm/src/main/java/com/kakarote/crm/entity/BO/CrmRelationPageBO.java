package com.kakarote.crm.entity.BO;

import com.kakarote.core.entity.PageEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrmRelationPageBO extends PageEntity {

    private Integer customerId;

    private Integer contractId;
}
