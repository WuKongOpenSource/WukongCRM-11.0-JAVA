package com.kakarote.core.feign.admin.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Ian
 * @date 2020/8/28
 */
@Data
@ApiModel("呼叫中心用户")
public class CallUser implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty("用户ID列表")
    private List<Long> userIds;

    @ApiModelProperty("状态")
    private Integer state;
}
