package com.kakarote.hrm.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 参保项目表
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_hrm_insurance_project")
@ApiModel(value="HrmInsuranceProject对象", description="参保项目表")
public class HrmInsuranceProject implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "项目id")
    @TableId(value = "project_id", type = IdType.AUTO)
    private Integer projectId;

    @ApiModelProperty(value = "参保方案id")
    private Integer schemeId;

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

    @ApiModelProperty(value = "1 删除 0 使用")
    private Integer isDel;





}
