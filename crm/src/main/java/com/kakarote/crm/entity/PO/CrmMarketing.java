package com.kakarote.crm.entity.PO;

import com.baomidou.mybatisplus.annotation.*;
import com.kakarote.core.common.RangeValidated;
import com.kakarote.core.servlet.upload.FileEntity;
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
 * 营销表
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-08-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_crm_marketing")
@ApiModel(value="CrmMarketing对象", description="营销表")
@RangeValidated(minFieldName = "startTime", maxFieldName = "endTime",message = "开始时间必须大于结束时间")
public class CrmMarketing implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "营销id")
    @TableId(value = "marketing_id", type = IdType.AUTO)
    private Integer marketingId;

    @ApiModelProperty(value = "营销名称")
    private String marketingName;

    @ApiModelProperty(value = "1线索  2客户")
    private Integer crmType;

    @ApiModelProperty(value = "截止时间")
    private Date endTime;

    @ApiModelProperty(value = "关联人员ID")
    private String relationUserId;

    @ApiModelProperty(value = "创建人ID")
    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @ApiModelProperty(value = "1启用  0禁用")
    private Integer status;

    @ApiModelProperty(value = "每个客户只能填写次数 0 1")
    private Integer second;

    @ApiModelProperty(value = "营销内容填写字段")
    private String fieldDataId;

    @ApiModelProperty(value = "浏览数")
    private Integer browse;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;


    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "分享数")
    private Integer shareNum;

    @ApiModelProperty(value = "提交数")
    private Integer submitNum;

    @ApiModelProperty(value = "简介")
    private String synopsis;

    @ApiModelProperty(value = "首图id")
    private String mainFileIds;

    private String detailFileIds;

    @ApiModelProperty(value = "活动地址")
    private String address;

    @ApiModelProperty(value = "活动类型")
    private String marketingType;

    @ApiModelProperty(value = "活动金额")
    private BigDecimal marketingMoney;

    @TableField(exist = false)
    private String createUserName;

    @TableField(exist = false)
    private String crmTypeName;

    @TableField(exist = false)
    private List<FileEntity> mainFileList;

    @TableField(exist = false)
    private FileEntity mainFile;


}
