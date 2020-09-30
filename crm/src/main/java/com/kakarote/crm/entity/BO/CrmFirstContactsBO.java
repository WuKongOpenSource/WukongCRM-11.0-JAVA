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
@ApiModel("crm首要联系人BO对象")
public class CrmFirstContactsBO {

    @ApiModelProperty("联系人ID")
    private Integer contactsId;

    @ApiModelProperty("客户ID")
    private Integer customerId;

    @ApiModelProperty("商机ID")
    private Integer businessId;
}
