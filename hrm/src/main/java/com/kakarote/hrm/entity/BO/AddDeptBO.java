package com.kakarote.hrm.entity.BO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "部门添加对象")
public class AddDeptBO {

    @TableId(value = "dept_id", type = IdType.AUTO)
    private Integer deptId;

    @ApiModelProperty(value = "父级ID 顶级部门为0")
    private Integer pid;

    @ApiModelProperty(value = "部门类型 1 公司 2 部门")
    @NotNull(message = "部门类型不能为空")
    private Integer deptType;

    @ApiModelProperty(value = "部门名称")
    @NotBlank(message = "部门名称不能为空")
    private String name;

    @ApiModelProperty(value = "部门编码")
    @NotBlank(message = "部门编码不能为空")
    private String code;

    @ApiModelProperty(value = "部门负责人ID")
    private Integer mainEmployeeId;

    @ApiModelProperty(value = "分管领导")
    private Integer leaderEmployeeId;

}
