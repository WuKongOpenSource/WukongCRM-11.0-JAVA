package com.kakarote.admin.entity.BO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeptVO {


    @ApiModelProperty(value = "部门id")
    private Integer deptId;

    @ApiModelProperty(value = "父级ID 顶级部门为0")
    private Integer pid;

    @ApiModelProperty(value = "部门名称")
    private String name;

    @ApiModelProperty("当前部门在职人数")
    private Integer currentNum;

    @ApiModelProperty("在职人数")
    private Integer allNum;

    @ApiModelProperty("是否有下级部门 0 否 1 是")
    private Integer hasChildren;

}
