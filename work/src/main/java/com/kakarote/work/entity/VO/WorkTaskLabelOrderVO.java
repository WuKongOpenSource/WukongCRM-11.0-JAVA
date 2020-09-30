package com.kakarote.work.entity.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wyq
 */
@Data
@ApiModel("任务标签排序信息")
public class WorkTaskLabelOrderVO {
    @ApiModelProperty("标签id")
    private Integer labelId;

    @ApiModelProperty(value = "标签名")
    private String name;

    @ApiModelProperty(value = "排序")
    private Integer orderNum;

    @ApiModelProperty(value = "颜色")
    private String color;
}
