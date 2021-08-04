package com.kakarote.hrm.entity.BO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyUniqueBO {

    @ApiModelProperty("字段id")
    private Integer fieldId;

    @ApiModelProperty("值")
    private String value;

    private Integer id;
    @ApiModelProperty("状态")
    private Integer status = 0;

}
