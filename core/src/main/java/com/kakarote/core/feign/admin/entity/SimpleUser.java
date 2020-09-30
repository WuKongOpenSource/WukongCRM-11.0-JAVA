package com.kakarote.core.feign.admin.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zhangzhiwei
 * 简单的用户对象
 */
@Data
@ApiModel("用户对象")
public class SimpleUser implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("头像")
    private String img;

    @ApiModelProperty("昵称")
    private String realname;
}
