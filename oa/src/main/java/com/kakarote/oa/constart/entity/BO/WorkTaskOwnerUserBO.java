package com.kakarote.oa.constart.entity.BO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wyq
 */
@Data
@ApiModel("任务参与人参数")
public class WorkTaskOwnerUserBO {

    @ApiModelProperty("任务id")
    private Integer taskId;

    @ApiModelProperty("参与人id")
    private String ownerUserId;
}
