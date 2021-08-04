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
 * 员工绩效考核
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_hrm_achievement_employee_appraisal")
@ApiModel(value="HrmAchievementEmployeeAppraisal对象", description="员工绩效考核")
public class HrmAchievementEmployeeAppraisal implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "employee_appraisal_id", type = IdType.AUTO)
    private Integer employeeAppraisalId;

    @ApiModelProperty(value = "员工id")
    private Integer employeeId;

    @ApiModelProperty(value = "绩效id")
    private Integer appraisalId;

    @ApiModelProperty(value = "考核状态 1 待填写 2 待目标确认 3 待评定 4 待结果确认 5 终止绩效")
    private Integer status;

    @ApiModelProperty(value = "评分")
    private BigDecimal score;

    @ApiModelProperty(value = "考核结果")
    private Integer levelId;

    @ApiModelProperty(value = "结果阅读状态 0 未读 1 已读")
    private Integer readStatus;

    @ApiModelProperty(value = "跟进员工id")
    private Integer followUpEmployeeId;

    @ApiModelProperty(value = "跟进员工排序")
    private Integer followSort;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "是否为草稿 0否 1是")
    private Integer isDraft;

}
