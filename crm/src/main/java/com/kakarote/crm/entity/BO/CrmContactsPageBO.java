package com.kakarote.crm.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString
@ApiModel("联系人查询")
public class CrmContactsPageBO extends PageEntity {

    @ApiModelProperty("客户ID")
    private Integer customerId;

    @ApiModelProperty("商机ID")
    private Integer businessId;

    @ApiModelProperty("搜索条件")
    private String search;

    @ApiModelProperty("审核状态")
    private Integer checkStatus;
}
