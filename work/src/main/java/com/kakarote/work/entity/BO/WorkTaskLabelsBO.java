package com.kakarote.work.entity.BO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wyq
 */
@Data
@ApiModel("任务设置标签参数")
public class WorkTaskLabelsBO {

    @ApiModelProperty("任务id")
    private Integer taskId;

    @ApiModelProperty("标签")
    private String labelId;
}
