package com.kakarote.hrm.entity.VO;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class DeptVO {

    @ApiModelProperty(value = "部门id")
    private Integer deptId;

    @ApiModelProperty(value = "父级ID 顶级部门为0")
    private Integer pid;

    @ApiModelProperty(value = "父级组织")
    private String pName;

    @ApiModelProperty(value = "1 公司 2 部门")
    private Integer deptType;

    @ApiModelProperty(value = "部门名称")
    private String name;

    @ApiModelProperty(value = "部门编码")
    private String code;

    @ApiModelProperty(value = "部门负责人ID")
    private Integer mainEmployeeId;

    @ApiModelProperty(value = "分管领导")
    private Integer leaderEmployeeId;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;




    @ApiModelProperty("分管领导")
    private String leadEmployeeName;

    @ApiModelProperty("部门负责人")
    private String mainEmployeeName;

    @ApiModelProperty("在职人数")
    private Integer allNum;

    @ApiModelProperty("全职人数")
    private Integer fullTimeNum;

    @ApiModelProperty("非全职人数")
    private Integer nuFullTimeNum;

    @ApiModelProperty("本部门在职人数")
    private Integer myAllNum;

    @ApiModelProperty("本部门全职人数")
    private Integer myFullTimeNum;

    @ApiModelProperty("本部门非全职人数")
    private Integer myNuFullTimeNum;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<DeptVO> children;

}
