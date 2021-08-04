package com.kakarote.hrm.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class QueryEmployeeListByAppraisalIdBO extends PageEntity {

    @ApiModelProperty("考核id")
    @NotNull
    private Integer appraisalId;

    @ApiModelProperty("员工名称")
    private String employeeName;

    @ApiModelProperty("部门id")
    private Integer deptId;

    @ApiModelProperty("等级id")
    private Integer levelId;

    @ApiModelProperty("考核状态 1 待填写 2 待目标确认 3 待结果评定 4 待结果确认")
    private Integer status;

}
