package com.kakarote.hrm.entity.VO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class SalaryGroupPageListVO {

    @ApiModelProperty(value = "薪资组id")
    private Integer groupId;

    @ApiModelProperty(value = "薪资组名称")
    private String groupName;

    @ApiModelProperty(value = "部门范围")
    private List<SimpleHrmDeptVO> deptList;

    @ApiModelProperty(value = "员工范围")
    private List<SimpleHrmEmployeeVO> employeeList;

    @ApiModelProperty(value = "月计薪标准")
    private String salaryStandard;

    @ApiModelProperty(value = "转正、调薪月规则")
    private String changeRule;

    @ApiModelProperty(value = "计税规则id")
    private Integer ruleId;

    @ApiModelProperty(value = "计税规则")
    private String ruleName;

}
