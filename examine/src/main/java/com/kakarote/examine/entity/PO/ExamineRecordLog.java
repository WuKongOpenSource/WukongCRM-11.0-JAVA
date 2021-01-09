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
 * 审核日志表
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-12-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_examine_record_log")
@ApiModel(value="ExamineRecordLog对象", description="审核日志表")
public class ExamineRecordLog implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "log_id", type = IdType.AUTO)
    private Integer logId;

    @ApiModelProperty(value = "审批ID")
    private Long examineId;

    @ApiModelProperty(value = "审批流程ID")
    private Integer flowId;

    @ApiModelProperty(value = "审批记录ID")
    private Integer recordId;

    @ApiModelProperty(value = "1 依次审批 2 会签 3 或签")
    private Integer type;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "审核状态0待审核、1通过、2拒绝、3审核中 4:撤回 5 未提交 6 创建 7 已删除 8 作废")
    private Integer examineStatus;

    @ApiModelProperty(value = "审核人ID")
    private Long examineUserId;

    @ApiModelProperty(value = "审核角色ID")
    private Integer examineRoleId;

    @ApiModelProperty(value = "创建人ID")
    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "批次ID")
    private String batchId;


}
