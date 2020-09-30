package com.kakarote.crm.entity.BO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CrmRelateBusinessBO {

    private  Integer contactsId;

    private List<Integer> businessIds;
}
