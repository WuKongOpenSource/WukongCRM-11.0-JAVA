package com.kakarote.hrm.entity.VO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class InsuranceSchemeListVO {

    @ApiModelProperty(value = "社保方案id")
    private Integer schemeId;

    @ApiModelProperty(value = "方案名称")
    private String schemeName;

    @ApiModelProperty(value = "参保城市")
    private String city;

    @ApiModelProperty(value = "参保类型 1 比例 2 金额")
    private Integer schemeType;

    @ApiModelProperty("个人社保")
    private BigDecimal personalInsuranceAmount;

    @ApiModelProperty("个人公积金")
    private BigDecimal personalProvidentFundAmount;

    @ApiModelProperty("公司社保")
    private BigDecimal corporateInsuranceAmount;

    @ApiModelProperty("公司公积金")
    private BigDecimal corporateProvidentFundAmount;

    @ApiModelProperty("使用人数")
    private Integer useCount;



}
