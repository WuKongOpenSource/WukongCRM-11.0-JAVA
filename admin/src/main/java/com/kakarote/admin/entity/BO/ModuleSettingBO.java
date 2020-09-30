package com.kakarote.admin.entity.BO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author zhangzhiwei
 * 模块设置VO
 */
@ToString
@Data
@ApiModel("应用管理设置")
public class ModuleSettingBO {

    @ApiModelProperty(value = "设置ID", required = true)
    @NotNull
    private Integer settingId;

    @ApiModelProperty(value = "状态 1:启用 0:停用", required = true,allowableValues = "0,1")
    @NotNull(message = "状态不能为空")
    @Max(1)
    @Min(0)
    private Integer status;

    @ApiModelProperty(value = "名称")
    private String name;

}
