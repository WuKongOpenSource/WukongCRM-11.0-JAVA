package com.kakarote.hrm.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueryInsuranceRecordListBO extends PageEntity {
    @ApiModelProperty("年份")
    private Integer year;
}
