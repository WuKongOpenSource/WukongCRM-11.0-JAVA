package com.kakarote.authorization.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author zhangzhiwe
 * 用户状态修改
 */
@Data
@ToString
@ApiModel("用户状态修改BO")
@Builder
public class AdminUserStatusBO {

    @ApiModelProperty("ids")
    private List<Long> ids;

    @ApiModelProperty("状态")
    private Integer status;

}
