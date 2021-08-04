package com.kakarote.hrm.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 考评规则等级
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_hrm_achievement_appraisal_score_level")
@ApiModel(value="HrmAchievementAppraisalScoreLevel对象", description="考评规则等级")
public class HrmAchievementAppraisalScoreLevel implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "level_id", type = IdType.AUTO)
    private Integer levelId;

    @ApiModelProperty(value = "考核id")
    private Integer appraisalId;

    @ApiModelProperty(value = "等级名称")
    private String levelName;

    @ApiModelProperty(value = "最小分数")
    private BigDecimal minScore;

    @ApiModelProperty(value = "最大分数")
    private BigDecimal maxScore;

    @ApiModelProperty(value = "最小人数比例")
    private Integer minNum;

    @ApiModelProperty(value = "最大人数比例")
    private Integer maxNum;

    private Integer sort;




}
