package com.kakarote.work.entity.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author wyq
 */
@Data
@ApiModel("根据标签分类项目任务列表信息")
public class WorkTaskByLabelVO {
    @ApiModelProperty("项目id")
    private Integer workId;

    @ApiModelProperty("项目名称")
    private String name;

    @ApiModelProperty("项目颜色")
    private String color;

    @ApiModelProperty("任务列表")
    private List<TaskInfoVO> list;
}
