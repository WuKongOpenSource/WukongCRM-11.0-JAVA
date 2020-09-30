package com.kakarote.admin.entity.BO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author zhangzhiwei
 * 模块设置VO
 */
@ToString
@Data
@ApiModel("日志欢迎语对象")
public class LogWelcomeSpeechBO {

    @ApiModelProperty(value = "设置ID", required = true)
    @NotNull
    private Integer settingId;

    @ApiModelProperty(value = "日志欢迎语", required = true)
    @NotNull
    @Size(max = 100)
    private String value;

}
