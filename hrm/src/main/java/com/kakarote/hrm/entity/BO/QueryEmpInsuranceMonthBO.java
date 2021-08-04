package com.kakarote.hrm.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueryEmpInsuranceMonthBO extends PageEntity {

    @ApiModelProperty("年")
    private Integer year;

    @ApiModelProperty("月")
    private Integer month;
}
