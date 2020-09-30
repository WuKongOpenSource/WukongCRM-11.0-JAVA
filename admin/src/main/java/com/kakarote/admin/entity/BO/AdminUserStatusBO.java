package com.kakarote.admin.entity.BO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
public class AdminUserStatusBO {

    @ApiModelProperty("ids")
    private List<Long> ids;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("密码")
    private String password;

}
