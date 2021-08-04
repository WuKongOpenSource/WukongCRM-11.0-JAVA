package com.kakarote.crm.entity.BO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Administrator
 */
@Data
@ApiModel("回款计划BO")
public class CrmReceivablesPlanBO {

    @ApiModelProperty("合同ID")
    private Integer contractId;

    @ApiModelProperty("客户ID")
    private Integer customerId;

    @ApiModelProperty("回款计划ID")
    private Integer receivablesId;

    @ApiModelProperty(value = "计划回款金额")
    private BigDecimal money;

    @ApiModelProperty(value = "计划回款日期")
    private String returnDate;

    @ApiModelProperty(value = "计划回款方式")
    private String returnType;

    @ApiModelProperty(value = "提前几天提醒")
    private Integer remind;

    @ApiModelProperty(value = "提醒日期")
    private String remindDate;

    @ApiModelProperty(value = "备注")
    private String remark;
}
