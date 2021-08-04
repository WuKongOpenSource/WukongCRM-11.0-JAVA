package com.kakarote.hrm.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryAttendancePageBO extends PageEntity {

    @ApiModelProperty("日期")
    private String date;

    @ApiModelProperty("部门id")
    private Integer deptId;

    @ApiModelProperty("搜索")
    private String search;
}
