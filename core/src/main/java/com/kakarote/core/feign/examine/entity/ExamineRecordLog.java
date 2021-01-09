package com.kakarote.core.feign.examine.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 审核日志表
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-12-04
 */
@Data
public class ExamineRecordLog implements Serializable {

    private static final long serialVersionUID=1L;

    private Integer logId;

    @ApiModelProperty(value = "审批ID")
    private Long examineId;

    @ApiModelProperty(value = "审批流程ID")
    private Integer flowId;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "审批记录ID")
    private Integer recordId;

    @ApiModelProperty(value = "1 依次审批 2 会签 3 或签")
    private Integer type;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "审核状态0待审核、1通过、2拒绝、3审核中 4:撤回 5 未提交 6 创建 7 已删除 8 作废")
    private Integer examineStatus;

    @ApiModelProperty(value = "审核人ID")
    private Long examineUserId;

    @ApiModelProperty(value = "审核角色ID")
    private Integer examineRoleId;

    @ApiModelProperty(value = "创建人ID")
    private Long createUserId;

    @ApiModelProperty(value = "批次ID")
    private String batchId;


}
