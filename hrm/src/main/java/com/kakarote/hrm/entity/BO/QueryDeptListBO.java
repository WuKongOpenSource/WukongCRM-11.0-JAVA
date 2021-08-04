package com.kakarote.hrm.entity.BO;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueryDeptListBO {

    @ApiModelProperty("部门名称")
    private String name;

    @ApiModelProperty(value = "类型 tree 树结构 update 编辑")
    private String type;

    @ApiParam(name = "id", value = "父级ID", required = true, example = "0")
    private Integer id;
}
