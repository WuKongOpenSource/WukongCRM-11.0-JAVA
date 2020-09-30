package com.kakarote.crm.entity.BO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
}
