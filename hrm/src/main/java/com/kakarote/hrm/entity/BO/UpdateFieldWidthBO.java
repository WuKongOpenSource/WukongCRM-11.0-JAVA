package com.kakarote.hrm.entity.BO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateFieldWidthBO {

    @ApiModelProperty("字段id")
    private Integer fieldId;

    @ApiModelProperty("宽度")
    private Integer width;
}
