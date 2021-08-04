package com.kakarote.hrm.entity.VO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class EvaluatoResultVO extends SimpleHrmEmployeeVO{
    @ApiModelProperty(value = "权重")
    private BigDecimal weight;

    @ApiModelProperty(value = "评分")
    private BigDecimal score;

    @ApiModelProperty(value = "考核等级id")
    private Integer levelId;

    @ApiModelProperty(value = "考核等级")
    private String levelName;

    @ApiModelProperty(value = "评语")
    private String evaluate;

    @ApiModelProperty("0 未评定 1 已评定")
    private Integer status;

    @ApiModelProperty("创建时间")
    private Date createTime;
}
