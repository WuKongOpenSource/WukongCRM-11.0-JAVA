package com.kakarote.work.entity.PO;

import com.baomidou.mybatisplus.annotation.*;
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
 * 任务评论表
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-06-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_work_task_comment")
@ApiModel(value="WorkTaskComment对象", description="任务评论表")
public class WorkTaskComment implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "评论表")
    @TableId(value = "comment_id", type = IdType.AUTO)
    private Integer commentId;

    @ApiModelProperty(value = "评论人ID")
    private Long userId;

    @ApiModelProperty(value = "内容(答案)")
    private String content;

    @ApiModelProperty(value = "新建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "主评论的id")
    private Integer mainId;

    @ApiModelProperty(value = "回复对象ID")
    private Long pid;

    @ApiModelProperty(value = "状态 ")
    private Integer status;

    @ApiModelProperty(value = "评论项目任务ID 或评论其他模块ID")
    private Integer typeId;

    @ApiModelProperty(value = "赞")
    private Integer favour;

    @ApiModelProperty(value = "评论分类 1：任务评论  2：日志评论")
    private Integer type;


    @ApiModelProperty(value = "评论用户")
    @TableField(exist = false)
    private SimpleUser user;

    @ApiModelProperty(value = "回复用户")
    @TableField(exist = false)
    private SimpleUser replyUser;

    @ApiModelProperty(value = "评论列表")
    @TableField(exist = false)
    private List<WorkTaskComment> childCommentList;


}
