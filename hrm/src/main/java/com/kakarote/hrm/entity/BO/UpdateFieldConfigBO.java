package com.kakarote.hrm.entity.BO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UpdateFieldConfigBO {

    @ApiModelProperty(value = "字段id")
    private Integer id;

    @ApiModelProperty(value = "字段排序")
    private Integer sort;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "是否隐藏 0、不隐藏 1、隐藏")
    private Integer isHide;

    @ApiModelProperty(value = "字段宽度")
    private Integer width;
}
