package com.kakarote.oa.constart.entity.BO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wyq
 */
@Data
@ApiModel("任务标签信息")
public class TaskLabelBO {
    @ApiModelProperty(value = "任务id")
    private Integer taskId;

    @ApiModelProperty(value = "标签id")
    private Integer labelId;

    @ApiModelProperty(value = "标签名称")
    private String labelName;

    @ApiModelProperty(value = "标签颜色")
    private String color;
}
