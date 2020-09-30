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
@ApiModel("项目任务看板负责人信息")
public class WorkTaskTemplateUserVO {
    @ApiModelProperty("负责人id")
    private Long userId;

    @ApiModelProperty("负责人名称")
    private String realname;

    @ApiModelProperty("头像")
    private String img;

    @ApiModelProperty("列表内任务总数")
    private Integer count;

    @ApiModelProperty("任务列表")
    private List<TaskInfoVO> list;
}
