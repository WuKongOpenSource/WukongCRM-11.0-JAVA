package com.kakarote.work.entity.BO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author wyq
 */
@Data
@ApiModel("归档已完成任务参数")
public class ArchiveTaskByOwnerBO {
    @ApiModelProperty("项目id")
    @NotNull
    private Integer workId;

    @ApiModelProperty("负责人id")
    @NotNull
    private Long userId;
}
