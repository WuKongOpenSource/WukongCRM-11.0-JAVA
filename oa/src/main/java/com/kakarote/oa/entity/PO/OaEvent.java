package com.kakarote.oa.entity.PO;

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
 * 日程表
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-07-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_oa_event")
@ApiModel(value="OaEvent对象", description="日程表")
public class OaEvent implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "event_id", type = IdType.AUTO)
    private Integer eventId;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "日程类型")
    private Integer typeId;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "参与人")
    private String ownerUserIds;

    @ApiModelProperty(value = "创建人ID")
    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "重复类型 1从不重复 2每天 3每周 4每月 5每年")
    private Integer repetitionType;

    @ApiModelProperty(value = "重复频率")
    private Integer repeatRate;

    @ApiModelProperty(value = "3:周/4:月")
    private String repeatTime;

    @ApiModelProperty(value = "结束类型 1从不 2重复次数 3结束日期")
    private Integer endType;

    @ApiModelProperty(value = "2:次数/3:时间")
    private String endTypeConfig;

    @ApiModelProperty(value = "循环开始时间")
    private Date repeatStartTime;

    @ApiModelProperty(value = "循环结束时间")
    private Date repeatEndTime;

    private String batchId;


    @ApiModelProperty("颜色")
    @TableField(exist = false)
    private String color;


}
