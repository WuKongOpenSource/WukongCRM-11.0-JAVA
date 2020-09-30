package com.kakarote.admin.entity.PO;

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
 * 客户规则
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-06-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_admin_config")
@ApiModel(value="AdminConfig对象", description="客户规则")
public class AdminConfig implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "setting_id", type = IdType.AUTO)
    private Integer settingId;

    @ApiModelProperty(value = "状态，0:不启用 1 ： 启用")
    private Integer status;

    @ApiModelProperty(value = "设置名称")
    private String name;

    @ApiModelProperty(value = "值")
    private String value;

    @ApiModelProperty(value = "描述")
    private String description;



}
