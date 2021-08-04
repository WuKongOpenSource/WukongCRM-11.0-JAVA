package com.kakarote.hrm.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 考核目标确认人
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_hrm_achievement_appraisal_target_confirmors")
@ApiModel(value="HrmAchievementAppraisalTargetConfirmors对象", description="考核目标确认人")
public class HrmAchievementAppraisalTargetConfirmors implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "target_confirmors_id", type = IdType.AUTO)
    private Integer targetConfirmorsId;

    @ApiModelProperty(value = "考核id")
    private Integer appraisalId;

    @ApiModelProperty(value = "1 员工本人 2 直属上级 3 所在部门负责人 4 上级部门负责人 5 指定目标确认人")
    private Integer type;

    @ApiModelProperty(value = "指定确认人id")
    private Integer employeeId;

    @ApiModelProperty(value = "步骤号 从小到大")
    private Integer sort;




    @TableField(exist = false)
    private String employeeName;

    @TableField(exist = false)
    private Integer status;


}
