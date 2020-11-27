package com.kakarote.work.entity.BO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author JiaS
 * @date 2020/11/6
 */
@Data
@ApiModel("项目查询参数")
public class WorkTaskQueryBO{

    @ApiModelProperty("项目名称")
    private String name;

    @ApiModelProperty("成员id列表")
    private List<Long> userIdList;

    @ApiModelProperty("项目id列表")
    private List<Integer> workIdList;

    @ApiModelProperty("标签id列表")
    private List<Integer> labelIdList;

    @ApiModelProperty("项目排序：1 最早创建 2 最近创建 3 最近更新 ")
    private Integer workSort;

    @ApiModelProperty("任务排序：1 最近创建 2 最近截止 3 最近更新 4 最高优先级")
    private Integer sort;

    @ApiModelProperty("1 今天 2 上周 3 上月 4 去年 5 自定义时间")
    private Integer type;

    @ApiModelProperty("开始时间")
    private String startTime;

    @ApiModelProperty("结束时间")
    private String endTime;
}
