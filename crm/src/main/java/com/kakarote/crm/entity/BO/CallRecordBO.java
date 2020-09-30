package com.kakarote.crm.entity.BO;

import com.kakarote.core.feign.crm.entity.BiParams;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Ian
 * @date 2020/8/27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("通话记录BO")
public class CallRecordBO extends BiParams {

    @ApiModelProperty(value = "通话时长(秒)")
    private Long talkTime;

    private String talkTimeCondition;
}
