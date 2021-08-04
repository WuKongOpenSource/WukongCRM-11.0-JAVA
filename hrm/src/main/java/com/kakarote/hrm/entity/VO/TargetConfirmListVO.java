package com.kakarote.hrm.entity.VO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TargetConfirmListVO {

    @ApiModelProperty("员工绩效id")
    private Integer employeeAppraisalId;

    @ApiModelProperty("绩效id")
    private Integer appraisalId;

    @ApiModelProperty("员工id")
    private Integer employeeId;

    @ApiModelProperty("员工名称")
    private String employeeName;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("部门")
    private String deptName;

    @ApiModelProperty("考核名称")
    private String appraisalName;


}
