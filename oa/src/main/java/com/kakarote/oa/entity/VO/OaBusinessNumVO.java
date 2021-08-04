package com.kakarote.oa.entity.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("oa数量统计VO")
@Data
public class OaBusinessNumVO {

    @ApiModelProperty("日志数量")
    private Integer logNum;

    @ApiModelProperty("审批数量")
    private Integer examineNum;

    @ApiModelProperty("任务数量")
    private Integer taskNum;

    @ApiModelProperty("跟进数量")
    private Integer activityNum;
}
