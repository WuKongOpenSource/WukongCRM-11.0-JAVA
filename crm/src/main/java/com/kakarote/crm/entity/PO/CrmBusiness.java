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
 * 商机表
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_crm_business")
@ApiModel(value="CrmBusiness对象", description="商机表")
public class CrmBusiness implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "business_id", type = IdType.AUTO)
    private Integer businessId;

    @ApiModelProperty(value = "商机状态组")
    private Integer typeId;

    @ApiModelProperty(value = "销售阶段")
    private Integer statusId;

    @ApiModelProperty(value = "下次联系时间")
    private Date nextTime;

    @ApiModelProperty(value = "客户ID")
    private Integer customerId;

    @ApiModelProperty(value = "首要联系人ID")
    private Integer contactsId;

    @ApiModelProperty(value = "预计成交日期")
    private Date dealDate;

    @ApiModelProperty(value = "跟进状态 0未跟进1已跟进")
    private Integer followup;

    @ApiModelProperty(value = "商机名称")
    private String businessName;

    @ApiModelProperty(value = "商机金额")
    private BigDecimal money;

    @ApiModelProperty(value = "整单折扣")
    private BigDecimal discountRate;

    @ApiModelProperty(value = "产品总金额")
    private BigDecimal totalPrice;

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

    @ApiModelProperty(value = "批次 比如附件批次")
    private String batchId;

    @ApiModelProperty(value = "只读权限")
    private String roUserId;

    @ApiModelProperty(value = "读写权限")
    private String rwUserId;

    @ApiModelProperty(value = "1赢单2输单3无效")
    private Integer isEnd;

    private String statusRemark;

    @ApiModelProperty(value = "1正常 3  删除")
    private Integer status;

    @ApiModelProperty(value = "最后跟进时间")
    private Date lastTime;



}
