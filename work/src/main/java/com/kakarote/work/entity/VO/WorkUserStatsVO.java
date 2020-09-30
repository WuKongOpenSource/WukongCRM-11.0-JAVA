package com.kakarote.work.entity.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wyq
 */
@Data
@ApiModel("项目成员任务统计信息")
public class WorkUserStatsVO {
    @ApiModelProperty("员工id")
    private Long userId;

    @ApiModelProperty("员工姓名")
    private String realname;

    @ApiModelProperty("员工头像")
    private String img;

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
    private Integer overdueRate;

    @ApiModelProperty("任务归档数")
    private Integer archive;
}
