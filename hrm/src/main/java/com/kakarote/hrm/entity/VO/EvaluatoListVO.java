package com.kakarote.hrm.entity.VO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EvaluatoListVO extends TargetConfirmListVO {

    @ApiModelProperty("0 待评定 1 已评定")
    private Integer status;
}
