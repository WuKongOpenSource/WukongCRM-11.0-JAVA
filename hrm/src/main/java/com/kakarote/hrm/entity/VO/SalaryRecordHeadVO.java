package com.kakarote.hrm.entity.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(description = "定薪调薪详情头部数据")
public class SalaryRecordHeadVO {

    @ApiModelProperty("员工id")
    private Integer employeeId;

    @ApiModelProperty("员工名称")
    private String employeeName;

    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty("岗位")
    private String post;

    @ApiModelProperty(value = "员工状态 1正式 2试用  3实习 4兼职 5劳务 6顾问 7返聘 8外包")
    private Integer employeeStatus;


}
