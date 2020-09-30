package com.kakarote.oa.entity.VO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventTaskVO {

    @ApiModelProperty("任务id")
    private Integer taskId;

    @ApiModelProperty("任务名称")
    private String name;

    @ApiModelProperty("开始时间")
    private Long startTime;

    @ApiModelProperty("结束时间")
    private Long endTime;

    @ApiModelProperty("日程类型")
    private Integer eventType;
}
