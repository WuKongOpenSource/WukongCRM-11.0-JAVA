package com.kakarote.admin.entity.BO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HrmAddUserBO {

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "部门ID")
    private Integer deptId;

    @ApiModelProperty("角色id")
    private String roleId;

    @ApiModelProperty(value = "上级ID")
    private Long parentId;

    @ApiModelProperty("员工id")
    private List<Integer> employeeIds;


}
