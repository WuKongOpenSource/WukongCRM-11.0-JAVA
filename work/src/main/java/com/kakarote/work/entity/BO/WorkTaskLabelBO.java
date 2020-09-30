package com.kakarote.work.entity.BO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wyq
 */
@Data
@ApiModel("任务移除标签参数")
public class WorkTaskLabelBO {

    @ApiModelProperty("标签id")
    private Integer labelId;

    @ApiModelProperty("任务id")
    private Integer taskId;

    @ApiModelProperty("标签名称")
    private String labelName;

    @ApiModelProperty("颜色")
    private String color;
}
