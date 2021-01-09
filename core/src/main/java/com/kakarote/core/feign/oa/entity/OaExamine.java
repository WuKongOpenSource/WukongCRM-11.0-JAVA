package com.kakarote.core.feign.oa.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 审批表
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-07-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_oa_examine")
@ApiModel(value="OaExamine对象", description="审批表")
public class OaExamine implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "examine_id", type = IdType.AUTO)
    private Integer examineId;

    @ApiModelProperty(value = "审批类型")
    private Integer categoryId;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty("审批状态")
    private Integer examineStatus;

    @ApiModelProperty(value = "请假类型")
    private String typeId;

    @ApiModelProperty(value = "差旅、报销总金额")
    private BigDecimal money;

    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty(value = "时长")
    private BigDecimal duration;

    @ApiModelProperty(value = "创建人ID")
    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ApiModelProperty(value = "附件批次id")
    private String batchId;

    @ApiModelProperty("审批记录id")
    private Integer examineRecordId;

}
