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
@ApiModel("项目任务统计")
public class WorkTaskStatsVO {

    @ApiModelProperty("任务总数")
    private Integer allCount;

    @ApiModelProperty("任务完成数")
    private Integer complete;

    @ApiModelProperty("任务完成率")
    private String completionRate;

    @ApiModelProperty("任务未完成数")
    private Integer unfinished;

    @ApiModelProperty("任务逾期数")
    private Integer overdue;

    @ApiModelProperty("任务逾期率")
    private String overdueRate;

    @ApiModelProperty("任务归档数")
    private Integer archive;
}
