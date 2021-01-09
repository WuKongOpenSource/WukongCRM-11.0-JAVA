package com.kakarote.crm.entity.PO;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.kakarote.core.feign.examine.entity.ExamineRecordSaveBO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-07-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_crm_invoice")
@ApiModel(value="CrmInvoice对象", description="")
public class CrmInvoice implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "发票id")
    @TableId(value = "invoice_id", type = IdType.AUTO)
    private Integer invoiceId;

    @ApiModelProperty(value = "发票申请编号")
    private String invoiceApplyNumber;

    @ApiModelProperty(value = "客户id")
    private Integer customerId;

    @ApiModelProperty(value = "客户名称")
    @TableField(exist = false)
    private String customerName;

    @ApiModelProperty(value = "合同id")
    private Integer contractId;

    @ApiModelProperty(value = "合同编号")
    @TableField(exist = false)
    private String contractNum;

    @ApiModelProperty(value = "合同金额")
    @TableField(exist = false)
    private String contractMoney;

    @ApiModelProperty(value = "开票金额")
    private BigDecimal invoiceMoney;

    @ApiModelProperty(value = "开票日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date invoiceDate;

    @ApiModelProperty(value = "开票类型")
    private Integer invoiceType;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "抬头类型 1单位 2个人")
    private Integer titleType;

    @ApiModelProperty(value = "开票抬头")
    private String invoiceTitle;

    @ApiModelProperty(value = "纳税识别号")
    private String taxNumber;

    @ApiModelProperty(value = "开户行")
    private String depositBank;

    @ApiModelProperty(value = "开户账户")
    private String depositAccount;

    @ApiModelProperty(value = "开票地址")
    private String depositAddress;

    @ApiModelProperty(value = "电话")
    private String telephone;

    @ApiModelProperty(value = "联系人名称")
    private String contactsName;

    @ApiModelProperty(value = "联系方式")
    private String contactsMobile;

    @ApiModelProperty(value = "邮寄地址")
    private String contactsAddress;

    @ApiModelProperty(value = "审批记录id")
    private Integer examineRecordId;

    @ApiModelProperty(value = "审核状态 0待审核、1通过、2拒绝、3审核中、4撤回")
    private Integer checkStatus;

    @ApiModelProperty(value = "负责人id")
    private Long ownerUserId;

    @ApiModelProperty(value = "负责人名称")
    @TableField(exist = false)
    private String ownerUserName;

    @ApiModelProperty(value = "创建人名称")
    @TableField(exist = false)
    private String createUserName;

    @ApiModelProperty(value = "发票号码")
    private String invoiceNumber;

    @ApiModelProperty(value = "实际开票日期")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    private Date realInvoiceDate;

    @ApiModelProperty(value = "物流单号")
    private String logisticsNumber;

    @ApiModelProperty(value = "开票状态")
    private Integer invoiceStatus;

    @ApiModelProperty(value = "创建人id")
    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @TableField(exist = false)
    @ApiModelProperty(value = "审核人ID")
    private Long checkUserId;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "批次id")
    private String batchId;


    @TableField(exist = false)
    private ExamineRecordSaveBO examineFlowData;

}
