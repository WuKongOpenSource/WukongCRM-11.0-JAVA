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
 * 定薪调薪记录表
 * </p>
 *
 * @author hmb
 * @since 2020-11-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_hrm_salary_change_record")
@ApiModel(value="HrmSalaryChangeRecord对象", description="定薪调薪记录表")
public class HrmSalaryChangeRecord implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "员工id")
    private Integer employeeId;

    @ApiModelProperty(value = "记录类型 1 定薪 2 调薪")
    private Integer recordType;

    @ApiModelProperty(value = "调薪原因 0 入职定薪 1 入职核定 2 转正 3 晋升 4 调动 5 年中调薪 6 年度调薪 7 特别调薪 8 其他")
    private Integer changeReason;

    @ApiModelProperty(value = "生效时间")
    private Date enableDate;

    @ApiModelProperty(value = "试用期调整前工资")
    private String proBeforeSum;

    @ApiModelProperty(value = "试用期调整后工资")
    private String proAfterSum;

    @ApiModelProperty(value = "试用期工资明细")
    private String proSalary;

    @ApiModelProperty(value = "正式调整前工资 json")
    private String beforeSum;

    @ApiModelProperty(value = "正式调整后工资")
    private String afterSum;

    @ApiModelProperty(value = "正式工资明细 json")
    private String salary;

    @ApiModelProperty(value = "状态 0 未生效 1 已生效 2 已取消")
    private Integer status;

    @ApiModelProperty("员工状态")
    private Integer employeeStatus;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "调整前总工资")
    private String beforeTotal;

    @ApiModelProperty(value = "调整后总工资")
    private String afterTotal;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;


}
