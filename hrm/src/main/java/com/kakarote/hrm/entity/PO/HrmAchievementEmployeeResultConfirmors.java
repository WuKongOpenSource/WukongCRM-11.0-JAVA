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
 * 绩效结果确认表
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_hrm_achievement_employee_result_confirmors")
@ApiModel(value="HrmAchievementEmployeeResultConfirmors对象", description="绩效结果确认表")
public class HrmAchievementEmployeeResultConfirmors implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "confirmors_id", type = IdType.AUTO)
    private Integer confirmorsId;

    private Integer employeeId;

    @ApiModelProperty(value = "绩效id")
    private Integer appraisalId;

    @ApiModelProperty(value = "0 未确认 1 已确认")
    private Integer status;

    private Integer sort;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;


}
