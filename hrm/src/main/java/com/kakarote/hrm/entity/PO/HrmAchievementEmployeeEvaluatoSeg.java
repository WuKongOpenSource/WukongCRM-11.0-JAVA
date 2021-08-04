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
 * 员工绩效考核项评定表
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_hrm_achievement_employee_evaluato_seg")
@ApiModel(value="HrmAchievementEmployeeEvaluatoSeg对象", description="员工绩效考核项评定表")
public class HrmAchievementEmployeeEvaluatoSeg implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "employee_evaluato_seg_id", type = IdType.AUTO)
    private Integer employeeEvaluatoSegId;

    @ApiModelProperty(value = "员工端考核id")
    private Integer employeeAppraisalId;

    @ApiModelProperty(value = "评定id")
    private Integer employeeEvaluatoId;

    @ApiModelProperty(value = "考核项id")
    private Integer segId;

    @ApiModelProperty(value = "评定人id")
    private Integer employeeId;

    @ApiModelProperty(value = "评分")
    private BigDecimal score;

    @ApiModelProperty(value = "评语")
    private String evaluate;

    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "评定人")
    @TableField(exist = false)
    private String employeeName;


}
