package com.kakarote.work.entity.BO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wyq
 */
@Data
@ApiModel
public class RemoveWorkOwnerUserBO {
    @ApiModelProperty("项目id")
    private Integer workId;

    @ApiModelProperty("员工id")
    private Long ownerUserId;
}
