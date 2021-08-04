package com.kakarote.hrm.entity.VO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SlipEmployeeVO {

    @ApiModelProperty(value = "员工薪资记录id")
    private Integer sEmpRecordId;

    @ApiModelProperty(value = "员工id")
    private Integer employeeId;

    @ApiModelProperty(value = "工号")
    private String jobNumber;

    @ApiModelProperty(value = "员工名称")
    private String employeeName;

    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty(value = "岗位")
    private String post;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty(value = "发送状态 0 未发送 1 已发送")
    private Integer sendStatus;

}
