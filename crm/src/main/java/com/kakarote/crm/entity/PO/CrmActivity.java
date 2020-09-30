package com.kakarote.crm.entity.PO;

import com.baomidou.mybatisplus.annotation.*;
import com.kakarote.core.feign.crm.entity.SimpleCrmEntity;
import com.kakarote.core.servlet.upload.FileEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * crm活动表
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_crm_activity")
@ApiModel(value="CrmActivity对象", description="crm活动表")
public class CrmActivity implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "活动id")
    @TableId(value = "activity_id", type = IdType.AUTO)
    private Integer activityId;

    @ApiModelProperty(value = "活动类型 1 跟进记录 2 创建记录 3 商机阶段变更 4 外勤签到")
    private Integer type;

    @ApiModelProperty(value = "跟进类型")
    @NotNull(message = "跟进类型不能为空")
    private String category;

    @ApiModelProperty(value = "活动类型 1 线索 2 客户 3 联系人 4 产品 5 商机 6 合同 7回款 8日志 9审批 10日程 11任务 12 发邮件")
    @NotNull(message = "类型不能为空")
    private Integer activityType;

    @ApiModelProperty(value = "活动类型Id")
    @NotNull(message = "类型id不能为空")
    private Integer activityTypeId;

    @ApiModelProperty(value = "活动内容")
    @NotNull(message = "内容不能为空")
    private String content;

    @ApiModelProperty(value = "关联商机")
    private String businessIds;

    @ApiModelProperty(value = "关联联系人")
    private String contactsIds;

    @ApiModelProperty(value = "下次联系时间")
    private Date nextTime;

    @ApiModelProperty(value = "0 删除 1 未删除")
    private Integer status;

    @ApiModelProperty(value = "经度")
    private String lng;

    @ApiModelProperty(value = "纬度")
    private String lat;

    @ApiModelProperty(value = "签到地址")
    private String address;

    @ApiModelProperty(value = "创建人id")
    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "批次id")
    private String batchId;
    @ApiModelProperty(value = "昵称")
    @TableField(exist = false)
    private String realname;

    @ApiModelProperty(value = "头像")
    @TableField(exist = false)
    private String userImg;

    @ApiModelProperty(value = "客户ID")
    @TableField(exist = false)
    private Integer customerId;

    @ApiModelProperty(value = "客户名称")
    @TableField(exist = false)
    private String customerName;

    @TableField(exist = false)
    private String activityTypeName;

    @TableField(exist = false)
    private String crmTypeName;

    @ApiModelProperty(value = "文件")
    @TableField(exist = false)
    private List<FileEntity> file;
    @ApiModelProperty(value = "图片")
    @TableField(exist = false)
    private List<FileEntity> img;

    @ApiModelProperty(value = "商机列表")
    @TableField(exist = false)
    private List<SimpleCrmEntity> businessList;

    @ApiModelProperty(value = "联系人列表")
    @TableField(exist = false)
    private List<SimpleCrmEntity> contactsList;
}
