package com.kakarote.hrm.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EvaluatoListBO extends PageEntity {

    @ApiModelProperty("0 待评定 1 已评定")
    private Integer status;
    @ApiModelProperty("绩效id")
    private Integer appraisalId;
    private String search;
}
