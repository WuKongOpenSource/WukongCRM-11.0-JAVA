package com.kakarote.crm.entity.BO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * @author zhangzhiwei
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@ApiModel(value = "商机负责人变更BO")
public class CrmBusinessChangOwnerUserBO extends CrmChangeOwnerUserBO {

    @ApiModelProperty("转移类型 1 移出团队，2 变为团队成员")
    private Integer transferType;

    @ApiModelProperty("权限 1 只读，2 读写")
    private Integer power;

    @ApiModelProperty("变更类型 1、联系人 2、商机 3、合同")
    private List<Integer> changeType;
}
