package com.kakarote.hrm.entity.PO;

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
 * 面试表
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_hrm_recruit_interview")
@ApiModel(value="HrmRecruitInterview对象", description="面试表")
public class HrmRecruitInterview implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "面试id")
    @TableId(value = "interview_id", type = IdType.AUTO)
    private Integer interviewId;

    @ApiModelProperty(value = "候选人id")
    private Integer candidateId;

    @ApiModelProperty(value = "面试方式 1现场面试 2电话面试 3视频面试")
    @TableField(value = "type", updateStrategy = FieldStrategy.IGNORED)
    private Integer type;

    @ApiModelProperty(value = "面试轮次")
    private Integer stageNum;

    @ApiModelProperty(value = "面试官id")
    @TableField(value = "interview_employee_id", updateStrategy = FieldStrategy.IGNORED)
    private Integer interviewEmployeeId;

    @ApiModelProperty(value = "其他面试官")
    @TableField(value = "other_interview_employee_ids", updateStrategy = FieldStrategy.IGNORED)
    private String otherInterviewEmployeeIds;

    @ApiModelProperty(value = "面试时间")
    private Date interviewTime;

    @ApiModelProperty(value = "面试地址")
    private String address;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "面试情况 1面试未完成 2面试通过 3面试未通过 4 面试取消")
    private Integer result;

    @ApiModelProperty(value = "评价")
    private String evaluate;

    @ApiModelProperty(value = "取消原因")
    private String cancelReason;

    @ApiModelProperty(value = "创建人id")
    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;





}
