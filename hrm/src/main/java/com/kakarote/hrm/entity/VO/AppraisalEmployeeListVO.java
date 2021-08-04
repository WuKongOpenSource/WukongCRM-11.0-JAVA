package com.kakarote.hrm.entity.VO;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AppraisalEmployeeListVO {

    @ApiModelProperty("员工id")
    private Integer employeeId;

    @ApiModelProperty("员工名称")
    private String employeeName;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty("岗位")
    private String post;

    @ApiModelProperty("员工状态 1正式 2试用  3实习 4兼职 5劳务 6顾问 7返聘 8外包")
    private Integer employeeStatus;


    @ApiModelProperty(value = "入职时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date becomeTime;

    @ApiModelProperty(value = "已归档绩效")
    private Integer apprailsaledCnt;

    @ApiModelProperty(value = "进行中绩效")
    private Integer apprailsalingCnt;

    @ApiModelProperty(value = "最近绩效方案")
    private String lastAppraisalName;

    @ApiModelProperty(value = "最近绩效等级及分数")
    private String lastAppraisalResult;

    private Integer isDel;
}
