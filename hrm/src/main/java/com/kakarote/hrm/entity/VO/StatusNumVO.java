package com.kakarote.hrm.entity.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@ApiModel(description = "状态数量统计")
public class StatusNumVO {

    @ApiModelProperty("状态名称")
    private String label;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("数量")
    private Integer count;
}
