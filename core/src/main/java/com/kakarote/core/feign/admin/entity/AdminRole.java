package com.kakarote.core.feign.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-06-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_admin_role")
@ApiModel(value="AdminRole对象", description="角色表")
public class AdminRole implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "role_id", type = IdType.AUTO)
    private Integer roleId;

    @ApiModelProperty(value = "名称")
    private String roleName;

    @ApiModelProperty(value = "0、自定义角色1、管理角色 2、客户管理角色 3、人事角色 4、财务角色 5、项目角色 8、项目自定义角色")
    private Integer roleType;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "1 启用 0 禁用")
    private Integer status;

    @ApiModelProperty(value = "数据权限 1、本人，2、本人及下属，3、本部门，4、本部门及下属部门，5、全部 ")
    private Integer dataType;

    @ApiModelProperty(value = "0 隐藏 1 不隐藏")
    private Integer isHidden;

    @ApiModelProperty(value = "1 系统项目管理员角色 2 项目管理角色 3 项目编辑角色 4 项目只读角色")
    private Integer label;

    @TableField(exist = false)
    @ApiModelProperty(value = "菜单列表")
    private Map<String,List<Integer>> rules;

    @TableField(exist = false)
    @ApiModelProperty(value = "菜单id列表")
    private List<Integer> menuIds = new ArrayList<>();

}
