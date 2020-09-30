package com.kakarote.crm.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 场景默认关系表
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-06-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_crm_scene_default")
@ApiModel(value="CrmSceneDefault对象", description="场景默认关系表")
public class CrmSceneDefault implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "default_id", type = IdType.AUTO)
    private Integer defaultId;

    @ApiModelProperty(value = "类型")
    private Integer type;

    @ApiModelProperty(value = "人员ID")
    private Long userId;

    @ApiModelProperty(value = "场景ID")
    private Integer sceneId;



}
