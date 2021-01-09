package com.kakarote.oa.entity.PO;

import com.baomidou.mybatisplus.annotation.*;
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

    @ApiModelProperty(value = "请假类型")
    private String typeId;

    @ApiModelProperty(value = "差旅、报销总金额")
    private BigDecimal money;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "时长")
    private BigDecimal duration;

    @ApiModelProperty(value = "创建人ID")
    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "附件批次id")
    private String batchId;


    @ApiModelProperty(value = "审核记录ID")
    private Integer examineRecordId;

    @ApiModelProperty("审批状态")
    private Integer examineStatus;

}
