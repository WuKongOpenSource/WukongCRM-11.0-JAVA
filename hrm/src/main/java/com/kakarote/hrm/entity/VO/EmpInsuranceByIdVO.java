package com.kakarote.hrm.entity.VO;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class EmpInsuranceByIdVO {

    @ApiModelProperty("参保城市")
    private String city;

    @ApiModelProperty("身份证号")
    private String idNumber;

    @ApiModelProperty("社保号")
    private String socialSecurityNum;

    @ApiModelProperty("公积金号")
    private String accumulationFundNum;

    @ApiModelProperty("社保方案id")
    private Integer schemeId;

    @ApiModelProperty("社保方案")
    private String schemeName;

    @ApiModelProperty(value = "参保类型 1 比例 2 金额")
    private Integer schemeType;

    private List<HrmInsuranceProjectBO> socialSecurityList;
    private List<HrmInsuranceProjectBO> providentFundList;



    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class HrmInsuranceProjectBO{

        private Integer empProjectRecordId;

        @ApiModelProperty(value = "项目id")
        private Integer projectId;

        @ApiModelProperty(value = "1 养老保险基数 2 医疗保险基数 3 失业保险基数 4 工伤保险基数 5 生育保险基数 6 补充大病医疗保险 7 补充养老保险 8 残保险 9 社保自定义 10 公积金 11 公积金自定义")
        private Integer type;

        @ApiModelProperty(value = "项目名称")
        private String projectName;

        @ApiModelProperty(value = "默认基数")
        private BigDecimal defaultAmount;

        @ApiModelProperty(value = "公司比例")
        private BigDecimal corporateProportion;

        @ApiModelProperty(value = "个人比例")
        private BigDecimal personalProportion;

        @ApiModelProperty(value = "公司缴纳金额")
        private BigDecimal corporateAmount;

        @ApiModelProperty(value = "个人缴纳金额")
        private BigDecimal personalAmount;
    }
}
