package com.kakarote.hrm.entity.PO;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 招聘候选人表
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_hrm_recruit_candidate")
@ApiModel(value="HrmRecruitCandidate对象", description="招聘候选人表")
public class HrmRecruitCandidate implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "候选人id")
    @TableId(value = "candidate_id", type = IdType.AUTO)
    private Integer candidateId;

    @ApiModelProperty(value = "候选人名称")
    private String candidateName;

    @ApiModelProperty(value = "手机")
    private String mobile;

    @ApiModelProperty(value = "性别 1男 2女")
    private Integer sex;

    @ApiModelProperty(value = "年龄")
    @Min(value = 0,message = "年龄必须大于等于0")
    private Integer age;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "职位id")
    private Integer postId;

    @ApiModelProperty(value = "面试轮次 ")
    private Integer stageNum;

    @ApiModelProperty(value = "工作年限")
    @Min(value = 0,message = "工作年限必须大于等于0")
    private Integer workTime;

    @ApiModelProperty(value = "学历 1小学 2初中 3高中 4大专 5本科 6硕士 7博士")
    private Integer education;

    @ApiModelProperty(value = "毕业院校")
    private String graduateSchool;

    @ApiModelProperty(value = "最近工作单位")
    private String latestWorkPlace;

    @ApiModelProperty(value = "招聘渠道")
    private Integer channelId;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "候选人状态 1 新候选人 2初选通过 3安排面试 4面试通过 5已发offer 6待入职 7已淘汰 8已入职")
    private Integer status;

    @ApiModelProperty(value = "淘汰原因")
    private String eliminate;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "创建人id")
    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @ApiModelProperty(value = "状态更新时间")
    private Date statusUpdateTime;

    @ApiModelProperty(value = "入职时间")
    private Date entryTime;

    @ApiModelProperty(value = "批次id")
    private String batchId;




    @ApiModelProperty("职位名称")
    @TableField(exist = false)
    private String postName;

    @ApiModelProperty("招聘渠道名称")
    @TableField(exist = false)
    private String channelName;


}
