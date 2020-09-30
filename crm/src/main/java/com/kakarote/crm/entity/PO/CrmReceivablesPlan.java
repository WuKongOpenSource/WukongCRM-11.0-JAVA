package com.kakarote.crm.entity.PO;

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
 * 回款计划表
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_crm_receivables_plan")
@ApiModel(value="CrmReceivablesPlan对象", description="回款计划表")
public class CrmReceivablesPlan implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "plan_id", type = IdType.AUTO)
    private Integer planId;

    @ApiModelProperty(value = "期数")
    private String num;

    @ApiModelProperty(value = "回款ID")
    private Integer receivablesId;

    @ApiModelProperty(value = "1完成 0 未完成")
    private Integer status;

    @ApiModelProperty(value = "计划回款金额")
    private BigDecimal money;

    @ApiModelProperty(value = "计划回款日期")
    private Date returnDate;

    @ApiModelProperty(value = "计划回款方式")
    private String returnType;

    @ApiModelProperty(value = "提前几天提醒")
    private Integer remind;

    @ApiModelProperty(value = "提醒日期")
    private Date remindDate;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建人ID")
    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @ApiModelProperty(value = "负责人ID")
    private Long ownerUserId;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "附件批次ID")
    private String fileBatch;

    @ApiModelProperty(value = "合同ID")
    private Integer contractId;

    @ApiModelProperty(value = "客户ID")
    private Integer customerId;


    @ApiModelProperty(value = "客户名称")
    @TableField(exist = false)
    private String customerName;

    @ApiModelProperty(value = "合同期数")
    @TableField(exist = false)
    private String contractNum;


}
