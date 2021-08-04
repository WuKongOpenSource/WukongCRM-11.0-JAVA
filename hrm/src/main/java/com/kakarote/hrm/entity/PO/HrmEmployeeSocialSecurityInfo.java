package com.kakarote.hrm.entity.PO;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 员工公积金信息
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_hrm_employee_social_security_info")
@ApiModel(value="HrmEmployeeSocialSecurityInfo对象", description="员工公积金信息")
public class HrmEmployeeSocialSecurityInfo implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "social_security_info_id", type = IdType.AUTO)
    private Integer socialSecurityInfoId;

    private Integer employeeId;

    @ApiModelProperty(value = "是否首次缴纳社保 0 否 1 是")
    private Integer isFirstSocialSecurity;

    @ApiModelProperty(value = "是否首次缴纳公积金 0 否 1 是")
    private Integer isFirstAccumulationFund;

    @ApiModelProperty(value = "社保号")
    private String socialSecurityNum;

    @ApiModelProperty(value = "公积金账号")
    private String accumulationFundNum;

    @ApiModelProperty(value = "参保起始月份（2020.05）")
    private String socialSecurityStartMonth;

    @ApiModelProperty(value = "参保方案")
    private Integer schemeId;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(exist = false)
    @ApiModelProperty(value = "参保方案名称")
    private String schemeName;


}
