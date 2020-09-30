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
 * 
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-07-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_crm_invoice_info")
@ApiModel(value="CrmInvoiceInfo对象", description="")
public class CrmInvoiceInfo implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "发票信息id")
    @TableId(value = "info_id", type = IdType.AUTO)
    private Integer infoId;

    @ApiModelProperty(value = "客户id")
    private Integer customerId;

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

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建人id")
    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;



}
