package com.kakarote.work.entity.PO;

import com.baomidou.mybatisplus.annotation.*;
import com.kakarote.work.entity.BO.WorkOwnerRoleBO;
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
 * 项目表
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-06-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_work")
@ApiModel(value="Work对象", description="项目表")
public class Work implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "项目ID")
    @TableId(value = "work_id", type = IdType.AUTO)
    private Integer workId;

    @ApiModelProperty(value = "项目名字")
    private String name;

    @ApiModelProperty(value = "状态 1启用 3归档 2 删除")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "创建人ID")
    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "颜色")
    private String color;

    @ApiModelProperty(value = "是否所有人可见 1 是 0 否")
    private Integer isOpen;

    @ApiModelProperty(value = "公开项目成员角色id")
    private Integer ownerRole;

    @ApiModelProperty(value = "归档时间")
    private Date archiveTime;

    @ApiModelProperty(value = "删除时间")
    private Date deleteTime;

    @ApiModelProperty(value = "是否是系统自带封面 0不是 1是")
    private Integer isSystemCover;

    @ApiModelProperty(value = "项目封面路径 仅系统自带封面需要")
    private String coverUrl;

    private String batchId;


    @ApiModelProperty(value = "项目成员")
    private String ownerUserId;

    @ApiModelProperty(value = "项目成员列表")
    @TableField(exist = false)
    private List<WorkOwnerRoleBO> WorkOwnerRoleList;


}
