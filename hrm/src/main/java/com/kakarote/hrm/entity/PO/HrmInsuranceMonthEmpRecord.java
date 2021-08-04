package com.kakarote.hrm.entity.PO;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 员工每月社保记录
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_hrm_insurance_month_emp_record")
@ApiModel(value="HrmInsuranceMonthEmpRecord对象", description="员工每月社保记录")
public class HrmInsuranceMonthEmpRecord implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "i_emp_record_id", type = IdType.AUTO)
    private Integer iEmpRecordId;

    @ApiModelProperty(value = "每月生成社保id")
    private Integer iRecordId;

    @ApiModelProperty(value = "员工id")
    private Integer employeeId;

    @ApiModelProperty(value = "社保方案id")
    private Integer schemeId;

    @ApiModelProperty(value = "年")
    private Integer year;

    @ApiModelProperty(value = "月")
    private Integer month;

    @ApiModelProperty(value = "个人社保金额")
    private BigDecimal personalInsuranceAmount;

    @ApiModelProperty(value = "个人公积金金额")
    private BigDecimal personalProvidentFundAmount;

    @ApiModelProperty(value = "公司社保金额")
    private BigDecimal corporateInsuranceAmount;

    @ApiModelProperty(value = "公司社保金额")
    private BigDecimal corporateProvidentFundAmount;

    @ApiModelProperty("0 停保 1 正常")
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;




}
