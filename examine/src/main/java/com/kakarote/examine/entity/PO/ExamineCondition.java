package com.kakarote.examine.entity.PO;

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
 * 审批条件表
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-11-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_examine_condition")
@ApiModel(value="ExamineCondition对象", description="审批条件表")
public class ExamineCondition implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "condition_id", type = IdType.AUTO)
    private Integer conditionId;

    @ApiModelProperty(value = "条件名称")
    private String conditionName;

    @ApiModelProperty(value = "审批流程ID")
    private Integer flowId;

    @ApiModelProperty(value = "优先级 数字越低优先级越高")
    private Integer priority;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "创建人ID")
    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @ApiModelProperty(value = "批次ID")
    private String batchId;


}
