package com.kakarote.hrm.entity.BO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(description = "查询员工自定义字段管理对象")
public class QueryEmployFieldManageBO {
    @ApiModelProperty(value = "入职状态 1 在职 2 待入职 ")
    private Integer entryStatus;
    @ApiModelProperty(value = "标签 1 个人信息 2 岗位信息 3 合同 4 工资社保")
    private Integer label;
}
