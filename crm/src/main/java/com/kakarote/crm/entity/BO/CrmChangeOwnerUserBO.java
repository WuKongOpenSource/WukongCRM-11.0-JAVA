package com.kakarote.crm.entity.BO;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
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

    @ApiModelProperty("转移类型 1 移出团队，2 变为团队成员")
    private Integer transferType;

    @ApiModelProperty("权限 1 只读，2 读写")
    private Integer power;

    @ApiModelProperty("变更类型 1、联系人 2、商机 3、合同")
    private List<Integer> changeType;

    @ApiModelProperty("过期时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expiresTime;
}
