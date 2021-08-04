package com.kakarote.hrm.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueryInsurancePageListBO extends PageEntity {

    @ApiModelProperty("每月社保记录id")
    private Integer iRecordId;

    @ApiModelProperty("员工名称")
    private String employeeName;

    @ApiModelProperty("参保方案id")
    private Integer schemeId;

    @ApiModelProperty("参保城市")
    private String city;

    @ApiModelProperty("0 停保 1 参保")
    private Integer status;
}
