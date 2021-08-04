package com.kakarote.hrm.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 计税规则
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_hrm_salary_tax_rule")
@ApiModel(value="HrmSalaryTaxRule对象", description="计税规则")
public class HrmSalaryTaxRule implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "rule_id", type = IdType.AUTO)
    @ApiModelProperty("计税规则id")
    private Integer ruleId;

    @ApiModelProperty(value = "规则名称")
    private String ruleName;

    @ApiModelProperty(value = "个税类型 1 工资薪金所得税 2 劳务报酬所得税 3 不计税")
    private Integer taxType;

    @ApiModelProperty(value = "是否计税 0 否 1 是")
    private Integer isTax;

    @ApiModelProperty(value = "起征点")
    private Integer markingPoint;

    @ApiModelProperty(value = "个税结果(保留小数点)")
    private Integer decimalPoint;

    @ApiModelProperty(value = "计税周期类型 1 上年12月到今年11月（对应的工资发放方式为次月发上月工资） 2 今年1月到12月（对应的工资发放方式为当月发当月工资）")
    private Integer cycleType;



    @ApiModelProperty("使用的薪资组")
    @TableField(exist = false)
    private Integer salaryGroupCount;


}
