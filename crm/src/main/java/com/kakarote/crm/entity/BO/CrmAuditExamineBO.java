package com.kakarote.crm.entity.BO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@ApiModel("crm审核对象")
public class CrmAuditExamineBO {

    @ApiModelProperty("审核记录ID")
    private Integer recordId;

    @ApiModelProperty("审核状态")
    private Integer status;

    @ApiModelProperty("相关审核ID")
    private Integer id;

    @ApiModelProperty("审核备注")
    private String remarks;

    @ApiModelProperty("下一审批人ID")
    private Long nextUserId;

    @ApiModelProperty("负责人ID")
    private Long ownerUserId;
}
