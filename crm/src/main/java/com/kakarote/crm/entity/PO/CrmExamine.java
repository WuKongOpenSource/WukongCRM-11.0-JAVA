package com.kakarote.crm.entity.PO;

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
 * 审批流程表
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_crm_examine")
@ApiModel(value="CrmExamine对象", description="审批流程表")
public class CrmExamine implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "examine_id", type = IdType.AUTO)
    private Integer examineId;

    @ApiModelProperty(value = "1 合同 2 回款 3发票")
    private Integer categoryType;

    @ApiModelProperty(value = "审核类型 1 固定审批 2 授权审批")
    private Integer examineType;

    @ApiModelProperty(value = "审批流名称")
    private String name;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "部门ID（0为全部）")
    private String deptIds;

    @ApiModelProperty(value = "员工ID")
    private String userIds;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "修改人")
    private Long updateUserId;

    @ApiModelProperty(value = "状态 1 启用 0 禁用 2 删除")
    private Integer status;

    @ApiModelProperty(value = "流程说明")
    private String remarks;



}
