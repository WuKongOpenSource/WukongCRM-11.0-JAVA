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
 * 客户表
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_crm_customer")
@ApiModel(value="CrmCustomer对象", description="客户表")
public class CrmCustomer implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "customer_id", type = IdType.AUTO)
    private Integer customerId;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "跟进状态 0未跟进1已跟进")
    private Integer followup;

    @ApiModelProperty(value = "1锁定")
    private Integer isLock;

    @ApiModelProperty(value = "下次联系时间")
    private Date nextTime;

    @ApiModelProperty(value = "成交状态 0 未成交 1 已成交")
    private Integer dealStatus;

    @ApiModelProperty(value = "成交时间")
    private Date dealTime;

    @ApiModelProperty(value = "首要联系人ID")
    private Integer contactsId;

    @ApiModelProperty(value = "手机")
    private String mobile;

    @ApiModelProperty(value = "电话")
    private String telephone;

    @ApiModelProperty(value = "网址")
    private String website;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建人ID")
    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @ApiModelProperty(value = "负责人ID")
    private Long ownerUserId;

    @ApiModelProperty(value = "只读权限")
    private String roUserId;

    @ApiModelProperty(value = "读写权限")
    private String rwUserId;

    @ApiModelProperty(value = "省市区")
    private String address;

    @ApiModelProperty(value = "定位信息")
    private String location;

    @ApiModelProperty(value = "详细地址")
    private String detailAddress;

    @ApiModelProperty(value = "地理位置经度")
    private String lng;

    @ApiModelProperty(value = "地理位置维度")
    private String lat;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "批次 比如附件批次")
    private String batchId;


    @ApiModelProperty(value = "客户状态 1 正常 2锁定 3删除")
    private Integer status;

    @ApiModelProperty(value = "最后跟进时间")
    private Date lastTime;

    @ApiModelProperty(value = "放入公海时间")
    private Date poolTime;

    @ApiModelProperty(value = "1 分配 2 领取")
    private Integer isReceive;

    @ApiModelProperty(value = "最后一条跟进记录")
    private String lastContent;

    @ApiModelProperty(value = "接收到客户时间")
    private Date receiveTime;

    @ApiModelProperty(value = "进入公海前负责人id")
    private Long preOwnerUserId;


}
