package com.kakarote.crm.entity.BO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author zhangzhiwei
 * 负责人变更BO
 */
@Data
@ToString
@ApiModel(value = "负责人变更BO")
public class CrmChangeOwnerUserBO {

    @ApiModelProperty("变更的ID列表")
    private List<Integer> ids;

    @ApiModelProperty("新的负责人ID")
    private Long ownerUserId;
}
