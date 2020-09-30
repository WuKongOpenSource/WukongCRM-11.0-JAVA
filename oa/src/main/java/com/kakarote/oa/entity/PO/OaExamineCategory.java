package com.kakarote.oa.entity.PO;

import com.baomidou.mybatisplus.annotation.*;
import com.kakarote.core.feign.admin.entity.SimpleDept;
import com.kakarote.core.feign.admin.entity.SimpleUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 审批类型表
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-07-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_oa_examine_category")
@ApiModel(value="OaExamineCategory对象", description="审批类型表")
public class OaExamineCategory implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "category_id", type = IdType.AUTO)
    private Integer categoryId;

    @ApiModelProperty(value = "名称")
    private String title;

    @ApiModelProperty(value = "描述")
    private String remarks;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "1 普通审批 2 请假审批 3 出差审批 4 加班审批 5 差旅报销 6 借款申请 0 自定义审批")
    private Integer type;

    @ApiModelProperty(value = "创建人ID")
    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @ApiModelProperty(value = "1启用，0禁用")
    private Integer status;

    @ApiModelProperty(value = "1为系统类型，不能删除")
    private Integer isSys;

    @ApiModelProperty(value = "1固定2自选")
    private Integer examineType;

    @ApiModelProperty(value = "可见范围（员工）")
    private String userIds;

    @ApiModelProperty(value = "可见范围（部门）")
    private String deptIds;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "1已删除")
    private Integer isDeleted;

    @ApiModelProperty(value = "删除时间")
    private Date deleteTime;

    @ApiModelProperty(value = "删除人ID")
    private Long deleteUserId;


    @TableField(exist = false)
    private List<OaExamineStep> stepList;

    @TableField(exist = false)
    private List<SimpleUser> userList;

    @TableField(exist = false)
    private List<SimpleDept> deptList;

}
