package com.kakarote.hrm.entity.PO;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 离职信息
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_hrm_employee_quit_info")
@ApiModel(value="HrmEmployeeQuitInfo对象", description="离职信息")
public class HrmEmployeeQuitInfo implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "quit_info_id", type = IdType.AUTO)
    @ApiModelProperty(value = "离职id")
    private Integer quitInfoId;

    @ApiModelProperty(value = "员工id")
    private Integer employeeId;

    @ApiModelProperty(value = "计划离职日期")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    private Date planQuitTime;

    @ApiModelProperty(value = "申请离职日期")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    private Date applyQuitTime;

    @ApiModelProperty(value = "薪资结算日期")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    private Date salarySettlementTime;

    @ApiModelProperty(value = "离职类型 1 主动离职 2 被动离职 3 退休")
    private Integer quitType;

    @ApiModelProperty(value = "离职原因 1家庭原因 2身体原因 3薪资原因 4交通不便 5工作压力 6管理问题 7无晋升机会 8职业规划 9合同到期放弃续签 10其他个人原因  11试用期内辞退 12违反公司条例 13组织调整/裁员 14绩效不达标辞退 15合同到期不续签 16 其他原因被动离职")
    private Integer quitReason;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty("离职之前员工状态")
    private Integer oldStatus;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;




}
