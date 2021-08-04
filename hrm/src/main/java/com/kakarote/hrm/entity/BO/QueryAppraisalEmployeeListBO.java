package com.kakarote.hrm.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class QueryAppraisalEmployeeListBO extends PageEntity {

    @ApiModelProperty("员工名称")
    private String employeeName;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("员工状态 1正式 2试用  3实习 4兼职 5劳务 6顾问 7返聘 8外包")
    private Integer employeeStatus;

    @ApiModelProperty("部门")
    private Integer deptId;
}
