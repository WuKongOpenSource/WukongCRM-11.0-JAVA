package com.kakarote.hrm.entity.BO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UpdateAppraisalStatusBO {

    @ApiModelProperty("考核id")
    @NotNull
    private Integer appraisalId;

    @ApiModelProperty(value = "绩效状态  1 绩效填写中 2 绩效评定中 3 结果确认中 4 归档")
    @NotNull
    private Integer status;
}
