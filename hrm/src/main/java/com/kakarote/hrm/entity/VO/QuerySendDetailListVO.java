package com.kakarote.hrm.entity.VO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuerySendDetailListVO {

    private Integer id;

    @ApiModelProperty("员工名称")
    private String employeeName;

    @ApiModelProperty("工号")
    private String jobNumber;

    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty("岗位")
    private String post;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("查看状态 0 未查看 1 已查看")
    private Integer readStatus;

    @ApiModelProperty("备注")
    private String remarks;
}
