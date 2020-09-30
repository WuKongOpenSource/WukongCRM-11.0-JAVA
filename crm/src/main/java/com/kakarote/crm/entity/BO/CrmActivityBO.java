package com.kakarote.crm.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("活动记录BO")
public class CrmActivityBO extends PageEntity {

    @ApiModelProperty("crm类型")
    private Integer crmType;

    @ApiModelProperty("活动类型")
    private Integer activityType;

    @ApiModelProperty("活动类型ID")
    private Integer activityTypeId;

    private String search;

    private Integer intervalDay;

    private String startDate;

    private String endDate;
}
