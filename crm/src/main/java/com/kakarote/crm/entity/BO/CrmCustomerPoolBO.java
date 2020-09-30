package com.kakarote.crm.entity.BO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("放入公海BO")
public class CrmCustomerPoolBO {

    @ApiModelProperty("客户ID列表")
    private List<Integer> ids;

    @ApiModelProperty("公海ID")
    private Integer poolId;

    @ApiModelProperty("用户ID")
    private Long userId;
}
