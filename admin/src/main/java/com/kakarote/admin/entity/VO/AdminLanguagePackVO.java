package com.kakarote.admin.entity.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 语言包表
 * </p>
 *
 * @author zmj
 * @since 2020-12-02
 */
@Data
@ToString
@ApiModel("语言包列表查询返回")
public class AdminLanguagePackVO implements Serializable {


    @ApiModelProperty(value = "语言包id")
    private Integer languagePackId;

    @ApiModelProperty(value = "语言包名称")
    private String languagePackName;

    @ApiModelProperty(value = "创建人ID")
    private Long createUserId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "默认语言，0，否，1，是")
    private Integer defaultLanguage = 0;
}
