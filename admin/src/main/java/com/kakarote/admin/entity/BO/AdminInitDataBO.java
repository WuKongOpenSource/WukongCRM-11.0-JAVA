package com.kakarote.admin.entity.BO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author JiaS
 * @date 2020/11/17
 */
@Data
public class AdminInitDataBO {

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("临时码")
    private String temporaryCode;

    @ApiModelProperty("模块")
    private List<String> modules;

}
