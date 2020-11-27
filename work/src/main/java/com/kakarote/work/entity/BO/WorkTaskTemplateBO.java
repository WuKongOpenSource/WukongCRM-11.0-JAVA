package com.kakarote.work.entity.BO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author wyq
 */
@Data
@ApiModel("项目任务看板筛选信息")
public class WorkTaskTemplateBO {
    @ApiModelProperty("项目id")
    @NotNull
    private Integer workId;

    @ApiModelProperty("标签id数组")
    private List<Integer> labelId;

    @ApiModelProperty("成员id数组")
    private List<Long> mainUserId;

    @ApiModelProperty("截止时间类型")
    private Integer stopTimeType;

    @ApiModelProperty("任务名称关键字")
    private String taskName;

    @ApiModelProperty("1 手动 2 最近创建 3 最近截止 4 最近更新 5 最高优先级")
    private Integer sort;

    @ApiModelProperty("已完成任务排序")
    private Boolean completedTask;
}
