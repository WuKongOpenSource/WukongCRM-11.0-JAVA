package com.kakarote.work.entity.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wyq
 */
@Data
@Accessors(chain = true)
@ApiModel("项目任务列表统计信息")
public class WorkClassStatsVO {
    @ApiModelProperty("任务列表名称")
    private String className;

    @ApiModelProperty("任务完成数")
    private Integer complete;

    @ApiModelProperty("任务未完成数")
    private Integer undone;
}
