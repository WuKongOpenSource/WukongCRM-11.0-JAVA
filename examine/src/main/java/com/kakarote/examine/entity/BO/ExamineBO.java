package com.kakarote.examine.entity.BO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("crm审核对象")
public class ExamineBO {

    @ApiModelProperty("审核记录ID")
    private Integer recordId;

    @ApiModelProperty("审核流程ID")
    private Integer flowId;

    @ApiModelProperty("审核状态")
    private Integer status;

    @ApiModelProperty("相关审核ID")
    private Integer typeId;

    @ApiModelProperty("审核备注")
    private String remarks;

    @ApiModelProperty("审核人ID")
    private Long examineUserId;


}
