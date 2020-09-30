package com.kakarote.oa.entity.PO;

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
 * 工作日志表
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-07-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_oa_log")
@ApiModel(value="OaLog对象", description="工作日志表")
public class OaLog implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "log_id", type = IdType.AUTO)
    private Integer logId;

    @ApiModelProperty(value = "日志类型（1日报，2周报，3月报）")
    private Integer categoryId;

    @ApiModelProperty(value = "日志标题")
    private String title;

    @ApiModelProperty(value = "日志内容")
    private String content;

    @ApiModelProperty(value = "明日工作内容")
    private String tomorrow;

    @ApiModelProperty(value = "遇到问题")
    private String question;

    @ApiModelProperty(value = "创建人ID")
    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "通知人")
    private String sendUserIds;

    @ApiModelProperty(value = "通知部门")
    private String sendDeptIds;

    @ApiModelProperty(value = "已读人")
    private String readUserIds;

    @ApiModelProperty(value = "文件批次ID")
    private String batchId;



}
