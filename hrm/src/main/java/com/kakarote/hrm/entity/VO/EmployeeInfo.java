package com.kakarote.hrm.entity.VO;

import com.kakarote.core.feign.admin.entity.AdminRole;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeInfo {


    private Integer employeeId;

    @ApiModelProperty(value = "员工姓名")
    private String employeeName;

    @ApiModelProperty(value = "手机")
    private String mobile;

    @ApiModelProperty(value = "性别 1 男 2 女")
    private Integer sex;

    @ApiModelProperty(value = "年龄")
    private Integer age;

    @ApiModelProperty(value = "工号")
    private String jobNumber;

    @ApiModelProperty(value = "部门ID")
    private Integer deptId;

    @ApiModelProperty(value = "部门")
    private String deptName;

    @ApiModelProperty(value = "直属上级ID")
    private Integer parentId;

    @ApiModelProperty(value = "直属上级")
    private Integer parentName;

    @ApiModelProperty(value = "职位")
    private String post;

    @ApiModelProperty("人资角色(如果为null只展示员工端,不为null去label字段选择展示管理或中层领导页面)")
    private AdminRole role;
}
