package com.kakarote.hrm.common.tax;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TaxEntity {
    @ApiModelProperty("税率")
    private Integer taxRate;

    @ApiModelProperty("速算扣除数")
    private Integer quickDeduction;
}
