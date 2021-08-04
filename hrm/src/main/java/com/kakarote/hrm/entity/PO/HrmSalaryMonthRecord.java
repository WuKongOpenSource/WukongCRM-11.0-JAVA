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
 * 每月薪资记录
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_hrm_salary_month_record")
@ApiModel(value = "HrmSalaryMonthRecord对象", description = "每月薪资记录")
public class HrmSalaryMonthRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "s_record_id", type = IdType.AUTO)
    private Integer sRecordId;

    @ApiModelProperty("报表标题")
    private String title;

    @ApiModelProperty(value = "年份")
    private Integer year;

    @ApiModelProperty(value = "月份")
    private Integer month;

    @ApiModelProperty(value = "计薪人数")
    private Integer num;

    @ApiModelProperty("计薪开始时间")
    private Date startTime;

    @ApiModelProperty("计薪结束日期")
    private Date endTime;

    @ApiModelProperty(value = "个人社保")
    private BigDecimal personalInsuranceAmount;

    @ApiModelProperty(value = "个人公积金")
    private BigDecimal personalProvidentFundAmount;

    @ApiModelProperty(value = "企业社保")
    private BigDecimal corporateInsuranceAmount;

    @ApiModelProperty(value = "企业公积金")
    private BigDecimal corporateProvidentFundAmount;

    @ApiModelProperty(value = "预计应发工资")
    private BigDecimal expectedPaySalary;

    @ApiModelProperty(value = "个人所得税")
    private BigDecimal personalTax;

    @ApiModelProperty(value = "预计实发工资")
    private BigDecimal realPaySalary;

    @ApiModelProperty("薪资项表头")
    private String optionHead;

    @ApiModelProperty("审批记录id")
    private Integer examineRecordId;
    @ApiModelProperty("状态 0待审核、1通过、2拒绝、3审核中 4:撤回 5 未提交 6 核算完成")
    private Integer checkStatus;

    @ApiModelProperty("创建人Id")
    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;




}
