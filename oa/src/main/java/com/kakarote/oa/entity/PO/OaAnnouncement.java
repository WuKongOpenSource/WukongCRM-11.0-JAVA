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
 * 公告表
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-07-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_oa_announcement")
@ApiModel(value="OaAnnouncement对象", description="公告表")
public class OaAnnouncement implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "announcement_id", type = IdType.AUTO)
    private Integer announcementId;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "创建人ID")
    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "通知部门")
    private String deptIds;

    @ApiModelProperty(value = "通知人")
    private String ownerUserIds;

    @ApiModelProperty(value = "已读用户")
    private String readUserIds;


    @ApiModelProperty(value = "用户Id")
    @TableField(exist = false)
    private Long userId;

    @ApiModelProperty(value = "用户名")
    @TableField(exist = false)
    private String username;

    @ApiModelProperty(value = "用户头像")
    @TableField(exist = false)
    private String img;

    @ApiModelProperty(value = "用户昵称")
    @TableField(exist = false)
    private String realname;

    @ApiModelProperty(value = "父级ID")
    @TableField(exist = false)
    private Long parentId;

    @ApiModelProperty(value = "是否已读")
    @TableField(exist = false)
    private Integer isRead;

    @ApiModelProperty(value = "通知部门")
    @TableField(exist = false)
    private List<SimpleDept> deptList;

    @ApiModelProperty(value = "通知人")
    @TableField(exist = false)
    private List<SimpleUser> ownerUserList;

}
