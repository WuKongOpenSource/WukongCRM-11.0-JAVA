package com.kakarote.hrm.entity.BO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel("高级筛选字段")
public class QueryFilterFieldBO {

    @ApiModelProperty("字段类型")
    private Integer type;
    @ApiModelProperty("条件")
    private Integer conditionType;
    @ApiModelProperty("值")
    private List<String> value;
    @ApiModelProperty("字段名称")
    private String name;
    @ApiModelProperty("开始时间")
    private String start;
    @ApiModelProperty("结束时间")
    private String end;
}
