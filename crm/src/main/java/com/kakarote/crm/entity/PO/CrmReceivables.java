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
 * 回款表
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_crm_receivables")
@ApiModel(value="CrmReceivables对象", description="回款表")
public class CrmReceivables implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "回款ID")
    @TableId(value = "receivables_id", type = IdType.AUTO)
    private Integer receivablesId;

    @ApiModelProperty(value = "回款编号")
    private String number;

    @ApiModelProperty(value = "回款计划ID")
    private Integer planId;

    @ApiModelProperty(value = "客户ID")
    private Integer customerId;

    @ApiModelProperty(value = "合同ID")
    private Integer contractId;

    @ApiModelProperty(value = "0待审核、1通过、2拒绝、3审核中 4:撤回 5 未提交")
    private Integer checkStatus;

    @ApiModelProperty(value = "审核记录ID")
    private Integer examineRecordId;

    @ApiModelProperty(value = "回款日期")
    private Date returnTime;

    @ApiModelProperty(value = "回款方式")
    private String returnType;

    @ApiModelProperty(value = "回款金额")
    private BigDecimal money;

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

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "批次")
    private String batchId;


}
