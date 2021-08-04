package com.kakarote.hrm.entity.BO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComputeSalaryDataBO {

    @ApiModelProperty("是否同步社保数据")
    private Boolean isSyncInsuranceData;

    @ApiModelProperty("薪资记录id")
    private Integer sRecordId;
}
