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
 * 工资条
 * </p>
 *
 * @author hmb
 * @since 2020-11-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_hrm_salary_slip")
@ApiModel(value="HrmSalarySlip对象", description="工资条")
public class HrmSalarySlip implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "工资条发放记录id")
    private Integer recordId;

    private Integer sEmpRecordId;

    @ApiModelProperty(value = "员工id")
    private Integer employeeId;

    private Integer year;

    private Integer month;

    @ApiModelProperty(value = "查看状态 0 未读 1 已读")
    private Integer readStatus;

    @ApiModelProperty(value = "实发工资")
    private BigDecimal realSalary;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "发放时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;


}
