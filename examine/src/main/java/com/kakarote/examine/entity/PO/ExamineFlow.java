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
 * 审批流程表
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-11-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_examine_flow")
@ApiModel(value="ExamineFlow对象", description="审批流程表")
public class ExamineFlow implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "审核流程ID")
    @TableId(value = "flow_id", type = IdType.AUTO)
    private Integer flowId;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "审批ID")
    private Long examineId;

    @ApiModelProperty(value = "0 条件 1 指定成员 2 主管 3 角色 4 发起人自选 5 连续多级主管")
    private Integer examineType;

    @ApiModelProperty(value = "审批找不到用户或者条件均不满足时怎么处理 1 自动通过 2 管理员审批")
    private Integer examineErrorHandling;

    @ApiModelProperty(value = "条件ID")
    private Integer conditionId;

    @ApiModelProperty(value = "执行顺序，不可为空")
    private Integer sort;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "用户ID")
    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @ApiModelProperty(value = "批次ID")
    private String batchId;


}
