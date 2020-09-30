package com.kakarote.oa.constart.entity.BO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wyq
 */
@Data
@ApiModel("任务标题参数")
public class WorkTaskNameBO {

    @ApiModelProperty("任务id")
    private Integer taskId;

    @ApiModelProperty("任务名称")
    private String name;
}
