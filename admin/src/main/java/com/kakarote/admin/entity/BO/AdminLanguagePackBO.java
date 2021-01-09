package com.kakarote.admin.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @author zmj
 * 语言包服务
 */
@Data
@ToString
public class AdminLanguagePackBO extends PageEntity {

    @ApiModelProperty(value = "语言包ID")
    private Integer languagePackId;

    @ApiModelProperty("语言包名称")
    private String languagePackName;

}
