package com.kakarote.work.entity.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author wyq
 */
@Data
@ApiModel("任务活动日志信息")
public class WorkTaskLogVO {

    @ApiModelProperty(value = "任务活动日志id")
    private Integer logId;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "操作人头像")
    private String img;

    @ApiModelProperty(value = "操作人姓名")
    private String realname;
}
