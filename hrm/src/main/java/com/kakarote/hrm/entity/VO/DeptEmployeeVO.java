package com.kakarote.hrm.entity.VO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeptEmployeeVO {


    @ApiModelProperty(value = "部门id")
    private Integer deptId;

    @ApiModelProperty(value = "父级ID 顶级部门为0")
    private Integer pid;

    @ApiModelProperty(value = "1 公司 2 部门")
    private Integer deptType;

    @ApiModelProperty(value = "部门名称")
    private String name;

    @ApiModelProperty(value = "部门编码")
    private String code;

    @ApiModelProperty("在职人数")
    private Integer allNum;

    @ApiModelProperty("是否有下级部门 0 否 1 是")
    private Integer hasChildren;

    @ApiModelProperty("当前部门在职人数")
    private Integer currentNum;

}
