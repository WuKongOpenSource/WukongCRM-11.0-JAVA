package com.kakarote.crm.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 角色字段授权表
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@TableName("wk_crm_role_field")
@ApiModel(value="CrmRoleField对象", description="角色字段授权表")
public class CrmRoleField implements Serializable {

    private static final long serialVersionUID=1L;

    public CrmRoleField(Integer label,Integer roleId, String fieldName, String name, Integer authLevel, Integer operateType, Integer fieldType) {
        this.label = label;
        this.roleId = roleId;
        this.fieldName = fieldName;
        this.name = name;
        this.authLevel = authLevel;
        this.operateType = operateType;
        this.fieldType = fieldType;
        this.maskType = 0;
    }

    public CrmRoleField(Integer label,Integer roleId, String fieldName, String name, Integer authLevel, Integer operateType, Integer fieldType, Integer type) {
        this.label = label;
        this.roleId = roleId;
        this.fieldName = fieldName;
        this.name = name;
        this.authLevel = authLevel;
        this.operateType = operateType;
        this.fieldType = fieldType;
        this.maskType = 0;
        this.type =type;
    }

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "角色id")
    private Integer roleId;

    @ApiModelProperty(value = "crm模块")
    private Integer label;

    @TableField(exist = false)
    @ApiModelProperty(value = "字段类型")
    private Integer type;

    @ApiModelProperty(value = "字段id")
    private Integer fieldId;

    @ApiModelProperty(value = "字段标识")
    private String fieldName;

    @ApiModelProperty(value = "字段名称")
    private String name;

    @ApiModelProperty(value = "权限 1不可编辑不可查看 2可查看不可编辑 3可编辑可查看")
    private Integer authLevel;

    @ApiModelProperty(value = "操作权限 1都可以设置 2只有查看权限可设置 3只有编辑权限可设置 4都不能设置")
    private Integer operateType;

    @ApiModelProperty(value = "掩码类型 0 都不隐藏 1 列表隐藏详情不隐藏 2 都隐藏")
    private Integer maskType;

    @ApiModelProperty(value = "  0自定义字段 1原始字段 2原始字段但值在data表 3关联表的字段 4系统字段")
    private Integer fieldType;
}
