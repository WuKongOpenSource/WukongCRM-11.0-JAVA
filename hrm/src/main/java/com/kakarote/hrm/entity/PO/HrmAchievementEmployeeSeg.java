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
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 员工绩效考核项
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_hrm_achievement_employee_seg")
@ApiModel(value="HrmAchievementEmployeeSeg对象", description="员工绩效考核项")
public class HrmAchievementEmployeeSeg implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "seg_id", type = IdType.AUTO)
    private Integer segId;

    private Integer employeeAppraisalId;

    private Integer employeeId;

    @ApiModelProperty("模板考核项id")
    private Integer tempSegId;

    @ApiModelProperty(value = "考核项名称")
    private String segName;

    @ApiModelProperty(value = "值")
    private String value;

    @ApiModelProperty(value = "是否固定 1 是 0 否")
    private Integer isFixed;

    @ApiModelProperty(value = "权重 -1 员工写权重比 0~100")
    private BigDecimal weight;

    @ApiModelProperty(value = "目标进度")
    private Integer schedule;

    @ApiModelProperty(value = "完成情况说明")
    private String explainDesc;

    private Integer sort;

    @TableField(exist = false)
    @ApiModelProperty(value = "考核子项")
    private List<HrmAchievementEmployeeSegItem> items;

    @TableField(exist = false)
    @ApiModelProperty(value = "考核项评分")
    private List<HrmAchievementEmployeeEvaluatoSeg> evaluatoSegList;


}
