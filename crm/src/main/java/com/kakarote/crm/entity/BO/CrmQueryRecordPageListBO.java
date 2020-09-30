package com.kakarote.crm.entity.BO;

import com.kakarote.core.entity.PageEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrmQueryRecordPageListBO extends PageEntity {
    private String type;
    private Long userId;
    private Integer deptId;
    private String startTime;
    private String endTime;
    private Integer isUser;


    private Integer dataType;
    private String crmType;
    private Integer queryType;
    private Integer subUser;
    private String search;
}
