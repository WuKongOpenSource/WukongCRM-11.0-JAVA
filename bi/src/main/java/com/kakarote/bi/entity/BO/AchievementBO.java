package com.kakarote.bi.entity.BO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("业绩目标查询BO")
@Data
public class AchievementBO {

    @ApiModelProperty("年份")
    private String year;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("类型")
    private Integer type;

    @ApiModelProperty("部门ID")
    private Integer deptId;

    @ApiModelProperty("用户ID")
    private Long userId;
}
