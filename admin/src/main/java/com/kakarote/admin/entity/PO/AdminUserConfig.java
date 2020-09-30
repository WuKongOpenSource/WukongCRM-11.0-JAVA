package com.kakarote.admin.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 用户配置表
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-06-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("wk_admin_user_config")
@ApiModel(value="AdminUserConfig对象", description="用户配置表")
public class AdminUserConfig implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "setting_id", type = IdType.AUTO)
    private Integer settingId;

    private Long userId;

    @ApiModelProperty(value = "状态，0:不启用 1 ： 启用")
    private Integer status;

    @ApiModelProperty(value = "设置名称")
    private String name;

    @ApiModelProperty(value = "值")
    private String value;

    @ApiModelProperty(value = "描述")
    private String description;



}
