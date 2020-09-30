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
 * 审核日志表
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_crm_examine_log")
@ApiModel(value="CrmExamineLog对象", description="审核日志表")
public class CrmExamineLog implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "log_id", type = IdType.AUTO)
    private Long logId;

    @ApiModelProperty(value = "审批记录ID")
    private Integer recordId;

    @ApiModelProperty(value = "审核步骤ID")
    private Long examineStepId;

    @ApiModelProperty(value = "审核状态 0 未审核 1 审核通过 2 审核拒绝3 撤回审核")
    private Integer examineStatus;

    @ApiModelProperty(value = "创建人")
    private Long createUser;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "审核人")
    private Long examineUser;

    @ApiModelProperty(value = "审核时间")
    private Date examineTime;

    @ApiModelProperty(value = "审核备注")
    private String remarks;

    @ApiModelProperty(value = "是否是撤回之前的日志 0或者null为新数据 1：撤回之前的数据")
    private Integer isRecheck;

    private Integer orderId;



}
