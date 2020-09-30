package com.kakarote.crm.entity.PO;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 场景
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-06-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_crm_scene")
@ApiModel(value="CrmScene对象", description="场景")
public class CrmScene implements Serializable {

    private static final long serialVersionUID=1L;

    public CrmScene() {
    }

    public CrmScene(Integer type, String name, Long userId, Integer sort, String data, Integer isHide, Integer isSystem, String bydata) {
        this.type = type;
        this.name = name;
        this.userId = userId;
        this.sort = sort;
        this.data = data;
        this.isHide = isHide;
        this.isSystem = isSystem;
        this.bydata = bydata;
    }

    @TableId(value = "scene_id", type = IdType.AUTO)
    private Integer sceneId;

    @ApiModelProperty(value = "分类")
    @NotNull
    private Integer type;

    @ApiModelProperty(value = "场景名称")
    @NotNull
    private String name;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "排序ID")
    private Integer sort;

    @ApiModelProperty(value = "属性值")
    @NotNull
    private String data;

    @ApiModelProperty(value = "1隐藏")
    private Integer isHide;

    @ApiModelProperty(value = "1系统0自定义")
    private Integer isSystem;

    @ApiModelProperty(value = "系统参数")
    private String bydata;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;


    @ApiModelProperty(value = "是否默认")
    @TableField(exist = false)
    private Integer isDefault;


}
