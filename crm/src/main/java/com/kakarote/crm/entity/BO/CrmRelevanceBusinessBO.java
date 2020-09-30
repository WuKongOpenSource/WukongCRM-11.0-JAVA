package com.kakarote.crm.entity.BO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author zhangzhiwei
 * 业务关联对象
 */
@Data
@ToString
@ApiModel("业务关联对象")
public class CrmRelevanceBusinessBO {

    @ApiModelProperty("业务ID")
    private Integer businessId;

    @ApiModelProperty("关联对象列表")
    private List<Integer> contactsIds;
}
