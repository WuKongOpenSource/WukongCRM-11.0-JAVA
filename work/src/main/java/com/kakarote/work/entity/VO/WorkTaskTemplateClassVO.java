package com.kakarote.work.entity.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author wyq
 */
@Data
@Accessors(chain = true)
@ApiModel("项目任务看板任务列表信息")
public class WorkTaskTemplateClassVO {

    @ApiModelProperty("任务列表id")
    private Integer classId;

    @ApiModelProperty("任务列表名称")
    private String className;

    @ApiModelProperty("列表内任务总数")
    private Integer count;

    @ApiModelProperty("任务列表")
    private List<TaskInfoVO> list;
}
