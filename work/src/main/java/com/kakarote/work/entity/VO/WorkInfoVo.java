package com.kakarote.work.entity.VO;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.kakarote.core.feign.admin.entity.SimpleUser;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author wyq
 */
@Data
public class WorkInfoVo{
    @ApiModelProperty(value = "项目ID")
    private Integer workId;

    @ApiModelProperty(value = "项目名字")
    private String name;

    @ApiModelProperty(value = "状态 1启用 3归档 2 删除")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

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

    @ApiModelProperty(value = "文件ID")
    private String fileId;

    private String batchId;

    @ApiModelProperty(value = "项目成员")
    private String ownerUserId;

    @ApiModelProperty(value = "项目成员2")
    private List<SimpleUser> ownerUser;

    @ApiModelProperty(value = "是否收藏 0未收藏 1已收藏")
    private Integer collect;

    @ApiModelProperty(value = "排序")
    private Integer orderNum;

    @ApiModelProperty(value = "权限列表")
    private JSONObject authList;

    private JSONObject permission;

}
