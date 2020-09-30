package com.kakarote.oa.entity.BO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteEventBO {
    @ApiModelProperty("删除类型 1 只删除本次 2 删除此系列")
    private Integer type;
    @ApiModelProperty("删除当前日程时间")
    private Long time;
    @ApiModelProperty("日程id")
    private Integer eventId;
    @ApiModelProperty("日程批次id")
    private String batchId;
}
