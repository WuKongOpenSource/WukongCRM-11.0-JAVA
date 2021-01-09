package com.kakarote.admin.entity.PO;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 语言包表
 * </p>
 *
 * @author zmj
 * @since 2020-12-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_admin_language_pack")
@ApiModel(value="AdminLanguagePack对象", description="语言包表")
public class AdminLanguagePack implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "语言包id")
    @TableId(value = "language_pack_id", type = IdType.AUTO)
    private Integer languagePackId;

    @ApiModelProperty(value = "语言包名称")
    private String languagePackName;

    @ApiModelProperty(value = "语言包内容")
    private String languagePackContext;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "创建人ID")
    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;
}
