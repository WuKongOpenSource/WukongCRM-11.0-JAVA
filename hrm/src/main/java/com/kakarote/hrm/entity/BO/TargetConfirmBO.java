package com.kakarote.hrm.entity.BO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TargetConfirmBO {

    @ApiModelProperty("员工绩效id")
    private Integer employeeAppraisalId;

    @ApiModelProperty("状态 1 提交 2 驳回目标")
    private Integer status;

    @ApiModelProperty(value = "驳回原因")
    private String rejectReason;
}
