package com.kakarote.work.entity.BO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author wyq
 */
@Data
@ApiModel("任务标题参数")
public class WorkTaskNameBO {

    @ApiModelProperty("任务id")
    private Integer taskId;

    @ApiModelProperty("任务名称")
    private String name;

    @ApiModelProperty("1 手动 2 最近创建 3 最近截止 4 最近更新 5 最高优先级")
    private Integer sort;

    @ApiModelProperty("已完成任务排序")
    private Boolean completedTask;

    @ApiModelProperty("成员id列表")
    private List<Integer> userIdList;

    @ApiModelProperty("截止时间类型")
    private Integer stopTimeType;

    @ApiModelProperty("项目id列表")
    private List<Integer> workIdList;

    @ApiModelProperty("标签id列表")
    private List<Integer> labelIdList;
}
