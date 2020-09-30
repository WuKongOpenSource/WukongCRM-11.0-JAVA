package com.kakarote.core.feign.jxc.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * <p>
 * 状态返回
 * </p>
 *
 * @author shenyi
 * @since 2020-05-13
 */
@Data
@ToString
@ApiModel(value = "JxcState对象", description = "状态返回")
public class JxcState {
    @ApiModelProperty("0 删除 1不删除 2 正在审核 3审核完成 4 拒绝 5 撤回 ")
    private Integer state;
    @ApiModelProperty("负责人")
    private Long ownerUserId;
}
