package com.kakarote.hrm.entity.VO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeArchivesFieldVO {

    @ApiModelProperty(value = "主键ID")
    private Integer fieldId;

    @ApiModelProperty(value = "字段名称")
    private String name;

    @ApiModelProperty(value = "标签分组 * 1 员工个人信息 2 通讯信息")
    private Integer labelGroup;

    @ApiModelProperty(value = "是否员工可见 0 否 1 是")
    private Integer isEmployeeVisible;

    @ApiModelProperty(value = "是否员工可修改 0 否 1 是")
    private Integer isEmployeeUpdate;
}
