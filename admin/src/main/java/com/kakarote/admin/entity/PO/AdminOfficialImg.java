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
 * 官网图片
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-06-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_admin_official_img")
@ApiModel(value="AdminOfficialImg对象", description="官网图片")
public class AdminOfficialImg implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "official_img_id", type = IdType.AUTO)
    private Integer officialImgId;

    @ApiModelProperty(value = "附件大小（字节）")
    private Long size;

    @ApiModelProperty(value = "创建人ID")
    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "文件真实路径")
    private String path;

    @ApiModelProperty(value = "文件路径")
    private String filePath;

    @ApiModelProperty(value = "1.官网设置 2.名片海报")
    private Integer type;

    private String name;

    @ApiModelProperty(value = "0")
    private Integer tactic;


}
