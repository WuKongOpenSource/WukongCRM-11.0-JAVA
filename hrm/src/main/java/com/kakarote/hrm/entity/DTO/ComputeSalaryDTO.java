package com.kakarote.hrm.entity.DTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("计算薪资随想")
public class ComputeSalaryDTO {

    private Integer id;

    @ApiModelProperty("code")
    private Integer code;

    @ApiModelProperty("父code")
    private Integer parentCode;

    @ApiModelProperty("值")
    private String value;

    @ApiModelProperty("0 减 1 加")
    private Integer isPlus;

    @ApiModelProperty("是否计税")
    private Integer isTax;

    @ApiModelProperty("是否固定")
    private Integer isFixed;

    @ApiModelProperty("薪资项名称")
    private String name;

    @ApiModelProperty("是否参与薪资计算")
    private Integer isCompute;



}
