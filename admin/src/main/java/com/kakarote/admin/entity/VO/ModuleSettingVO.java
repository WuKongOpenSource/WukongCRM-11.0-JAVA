package com.kakarote.admin.entity.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @author zhangzhiwei
 * 模块设置VO
 */
@ToString
@Data
@ApiModel("应用管理设置")
public class ModuleSettingVO {

    @ApiModelProperty(value = "设置ID", required = true)
    private Integer settingId;

    @ApiModelProperty(value = "模块", required = true)
    private String module;

    @ApiModelProperty(value = "状态 1:启用 0:停用", required = true,allowableValues = "0,1")
    private Integer status;

    @ApiModelProperty(value = "类型 1:普通应用 2:增值应用 3:未发布应用", required = true,allowableValues = "1,2,3")
    private String type;

    @ApiModelProperty(value = "名称", required = true)
    private String name;
}
