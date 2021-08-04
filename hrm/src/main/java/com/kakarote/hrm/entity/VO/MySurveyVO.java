package com.kakarote.hrm.entity.VO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class MySurveyVO {

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

    @ApiModelProperty("入职日期")
    private Date entryTime;

    @ApiModelProperty("转正日期")
    private Date becomeTime;

    @ApiModelProperty("头像")
    private String img;

    @ApiModelProperty("公司名称")
    private String companyName;

    @ApiModelProperty("入职天数")
    private Long entryDay;

    @ApiModelProperty("工资条备注")
    private String slipRemarks;
}
