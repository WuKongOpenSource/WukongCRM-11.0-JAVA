package com.kakarote.crm.entity.BO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @author Administrator
 */
@Data
@ToString
@ApiModel("crm联系人保存对象")
public class CrmBusinessStatusBO {

    @ApiModelProperty("商机ID")
    private Integer businessId;

    @ApiModelProperty("商机状态ID")
    private Integer statusId;

    @ApiModelProperty("1 赢单 2 输单 3 无效")
    private Integer isEnd;

    private String statusRemark;
}
