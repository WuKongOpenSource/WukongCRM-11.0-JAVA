package com.kakarote.admin.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QuerySysLogBO extends PageEntity {

    @ApiModelProperty("开始时间")
    private String startTime;

    @ApiModelProperty("结束时间")
    private String endTime;

    @ApiModelProperty("模块 [admin crm oa work hrm jxc]")
    private String model;

    @ApiModelProperty("子模块")
    private List<Integer> subModelLabels;

    @ApiModelProperty("用户id")
    private List<Long> userIds;

    @ApiModelProperty("类型 1 数据操作日志 2 系统操作日志")
    private Integer type;
}
