package com.kakarote.hrm.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueryRecruitPostPageListBO extends PageEntity {

    @ApiModelProperty("职位名称")
    private String postName;

    @ApiModelProperty("招聘状态 0 停止招聘 1 招聘中 ")
    private Integer status;

    @ApiModelProperty("工作城市")
    private String city;

    @ApiModelProperty("用人部门")
    private Integer deptId;

    @ApiModelProperty("负责人")
    private Integer ownerEmployeeId;

    @ApiModelProperty(value = "工作性质  1 全职 2实习 3兼职 4劳务派遣 5劳务 6派遣 7外包")
    private Integer jobNature;


}
