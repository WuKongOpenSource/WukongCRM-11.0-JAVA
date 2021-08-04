package com.kakarote.hrm.entity.BO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * <p>
 *
 * </p>
 *
 * @author guomenghao
 * @since 2021/03/19/10:56
 */
@Getter
@Setter
public class QueryLevelIdByScoreBO {
    @ApiModelProperty("员工考核id")
    @NotNull
    private Integer employeeAppraisalId;
    @ApiModelProperty(value = "评分")
    private BigDecimal score;
}
