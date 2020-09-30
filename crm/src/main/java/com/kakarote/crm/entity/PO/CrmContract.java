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
import java.util.List;

/**
 * <p>
 * 合同表
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_crm_contract")
@ApiModel(value="CrmContract对象", description="合同表")
public class CrmContract implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "contract_id", type = IdType.AUTO)
    private Integer contractId;

    @ApiModelProperty(value = "合同名称")
    private String name;

    @ApiModelProperty(value = "客户ID")
    private Integer customerId;

    @ApiModelProperty(value = "商机ID")
    private Integer businessId;

    @ApiModelProperty(value = "0待审核、1通过、2拒绝、3审核中 4:撤回 5 未提交 6 创建 7 已删除 8 作废")
    private Integer checkStatus;

    @ApiModelProperty(value = "审核记录ID")
    private Integer examineRecordId;

    @ApiModelProperty(value = "下单日期")
    private Date orderDate;

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

    @ApiModelProperty(value = "合同编号")
    private String num;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "合同金额")
    private BigDecimal money;

    @ApiModelProperty(value = "整单折扣")
    private BigDecimal discountRate;

    @ApiModelProperty(value = "产品总金额")
    private BigDecimal totalPrice;

    @ApiModelProperty(value = "合同类型")
    private String types;

    @ApiModelProperty(value = "付款方式")
    private String paymentType;

    @ApiModelProperty(value = "批次 比如附件批次")
    private String batchId;

    @ApiModelProperty(value = "只读权限")
    private String roUserId;

    @ApiModelProperty(value = "读写权限")
    private String rwUserId;

    @ApiModelProperty(value = "客户签约人（联系人id）")
    private Integer contactsId;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "公司签约人")
    private String companyUserId;


    @ApiModelProperty(value = "最后跟进时间")
    private Date lastTime;

    private BigDecimal receivedMoney;

    private BigDecimal unreceivedMoney;

    @ApiModelProperty(value = "产品列表")
    @TableField(exist = false)
    private List<CrmContractProduct> list;
}
