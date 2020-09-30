package com.kakarote.oa.entity.BO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuditExamineBO {

    private Integer recordId;

    private Integer status;

    private String remarks;

    private Long nextUserId;
}
