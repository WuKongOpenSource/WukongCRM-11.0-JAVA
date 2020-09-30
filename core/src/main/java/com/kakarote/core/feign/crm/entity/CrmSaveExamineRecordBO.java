package com.kakarote.core.feign.crm.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CrmSaveExamineRecordBO {

    @ApiModelProperty("类型 1合同，2回款 3 发票 4 薪资")
    private Integer type;

    @ApiModelProperty("审核用户ID")
    private Long userId;

    @ApiModelProperty("服务人id")
    private Long ownerUserId;

    @ApiModelProperty("审核记录id(兼容之前的参数,新建暂时传null)")
    private Integer recordId;

    @ApiModelProperty("审核状态(兼容之前的参数,新建暂时传null)")
    private Integer status;
}
