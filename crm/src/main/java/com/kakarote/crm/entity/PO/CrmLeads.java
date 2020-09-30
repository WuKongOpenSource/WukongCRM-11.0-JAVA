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
 * 线索表
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_crm_leads")
@ApiModel(value="CrmLeads对象", description="线索表")
public class CrmLeads implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "leads_id", type = IdType.AUTO)
    private Integer leadsId;

    @ApiModelProperty(value = "1已转化 0 未转化")
    private Integer isTransform;

    @ApiModelProperty(value = "跟进状态 0未跟进1已跟进")
    private Integer followup;

    @ApiModelProperty(value = "线索名称")
    private String leadsName;

    @ApiModelProperty(value = "客户id")
    private Integer customerId;

    @ApiModelProperty(value = "下次联系时间")
    private Date nextTime;

    @ApiModelProperty(value = "电话")
    private String telephone;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "地址")
    private String address;

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

    @ApiModelProperty(value = "1 分配")
    private Integer isReceive;

    @ApiModelProperty(value = "最后跟进时间")
    private Date lastTime;

    @ApiModelProperty(value = "最后一条跟进记录")
    private String lastContent;


}
