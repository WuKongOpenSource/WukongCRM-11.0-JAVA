package com.kakarote.hrm.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueryEmployeeByDeptIdBO extends PageEntity {

    @ApiModelProperty("部门名称")
    private Integer deptId;

    @ApiModelProperty("搜索")
    private String search;
}
