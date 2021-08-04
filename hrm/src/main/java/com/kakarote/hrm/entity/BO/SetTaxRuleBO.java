package com.kakarote.hrm.entity.BO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SetTaxRuleBO {

    @ApiModelProperty("计税规则id")
    private Integer ruleId;

    @ApiModelProperty(value = "计税周期类型 1 上年12月到今年11月（对应的工资发放方式为次月发上月工资） 2 今年1月到12月（对应的工资发放方式为当月发当月工资）")
    private Integer cycleType;
}
