package com.kakarote.hrm.entity.VO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class EmployeeListByAppraisalIdVO {

    @ApiModelProperty("员工id")
    private Integer employeeId;

    @ApiModelProperty("员工绩效id")
    private Integer employeeAppraisalId;

    @ApiModelProperty("员工名称")
    private String employeeName;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("工号")
    private String jobNumber;

    @ApiModelProperty("员工状态 1正式 2试用  3实习 4兼职 5劳务 6顾问 7返聘 8外包 9待离职 10已离职")
    private Integer employeeStatus;

    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty(value = "考核状态 1 待填写 2 待目标确认 3 待评定 4 待结果确认 5 终止绩效")
    private Integer status;

    @ApiModelProperty("跟进人")
    private String followUpEmployeeName;

    @ApiModelProperty(value = "评分")
    private BigDecimal score;

    @ApiModelProperty(value = "考核结果id")
    private Integer levelId;

    @ApiModelProperty(value = "考核结果")
    private String levelName;

    @ApiModelProperty(value = "结果阅读状态 0 未读 1 已读")
    private Integer readStatus;

    private Integer isDel;

}
