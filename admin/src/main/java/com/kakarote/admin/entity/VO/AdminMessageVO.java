package com.kakarote.admin.entity.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@ApiModel("消息数量VO")
@AllArgsConstructor
@NoArgsConstructor
public class AdminMessageVO {

    @ApiModelProperty("全部数量")
    private Integer allCount;

    @ApiModelProperty("公告数量")
    private Integer announceCount;

    @ApiModelProperty("审批数量")
    private Integer examineCount;

    @ApiModelProperty("任务数量")
    private Integer taskCount;

    @ApiModelProperty("日志数量")
    private Integer logCount;

    @ApiModelProperty("CRM数量")
    private Integer crmCount;

    @ApiModelProperty("日程数量")
    private Integer eventCount;

    @ApiModelProperty("知识库通知数量")
    private Integer knowledgeCount;

    @ApiModelProperty("进销存通知数量")
    private Integer jxcCount;
}
