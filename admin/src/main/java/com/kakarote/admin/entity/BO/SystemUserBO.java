package com.kakarote.admin.entity.BO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 初始话用户BO
 */
@Data
public class SystemUserBO implements Serializable {

    @ApiModelProperty("用户名")
    @NotNull
    private String username;

    @ApiModelProperty("凭证")
    @NotNull
    private String code;

    @ApiModelProperty("密码")
    @NotNull
    private String password;
}

