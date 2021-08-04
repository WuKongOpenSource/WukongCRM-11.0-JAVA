package com.kakarote.hrm.entity.PO;

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
 * 绩效考核
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_hrm_achievement_appraisal")
@ApiModel(value="HrmAchievementAppraisal对象", description="绩效考核")
public class HrmAchievementAppraisal implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "appraisal_id", type = IdType.AUTO)
    private Integer appraisalId;

    @ApiModelProperty(value = "考核名称")
    private String appraisalName;

    @ApiModelProperty(value = "1 月 2 季 3 年 4 半年")
    private Integer cycleType;

    @ApiModelProperty(value = "考核开始时间")
    private Date startTime;

    @ApiModelProperty(value = "考核结束时间")
    private Date endTime;

    @ApiModelProperty(value = "考核表模板id")
    private Integer tableId;

    @ApiModelProperty(value = "考核目标填写人 1 本人")
    private Integer writtenBy;

    @ApiModelProperty(value = "考核结果确认人")
    private String resultConfirmors;

    @ApiModelProperty(value = "考评总分数")
    private BigDecimal fullScore;

    @ApiModelProperty(value = "是否开启强制分布 1 是 0 否")
    private Integer isForce;

    @ApiModelProperty(value = "考核员工")
    private String employeeIds;

    @ApiModelProperty(value = "考核部门")
    private String deptIds;
    @ApiModelProperty(value = "考核步骤进度 -1 未开启 0 发起考核 1 确定目标 2 评定结果 3 确定结果")
    private Integer appraisalSteps;
    @ApiModelProperty(value = "进行中步骤进度 -1 未开启 0 发起考核 1 确定目标 2 评定结果 3 确定结果")
    private Integer activateSteps;
    @ApiModelProperty(value = "绩效状态 0 未开启考核 1 绩效填写中 2 绩效评定中 3 结果确认中 4 归档")
    private Integer status;

    @ApiModelProperty(value = "是否终止 0 否 1 是")
    private Integer isStop;

    @ApiModelProperty(value = "终止时间")
    private Date stopTime;


    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;



}
