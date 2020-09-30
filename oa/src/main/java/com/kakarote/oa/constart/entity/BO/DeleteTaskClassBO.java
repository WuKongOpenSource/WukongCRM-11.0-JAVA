package com.kakarote.oa.constart.entity.BO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author wyq
 */
@Data
@ApiModel("删除任务列表参数")
public class DeleteTaskClassBO {
    @ApiModelProperty("项目id")
    @NotNull
    private Integer workId;

    @ApiModelProperty("列表id")
    @NotNull
    private Integer classId;
}
