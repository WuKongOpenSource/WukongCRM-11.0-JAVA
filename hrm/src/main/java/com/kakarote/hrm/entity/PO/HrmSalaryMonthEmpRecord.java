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
 * 员工每月薪资记录
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_hrm_salary_month_emp_record")
@ApiModel(value = "HrmSalaryMonthEmpRecord对象", description = "员工每月薪资记录")
public class HrmSalaryMonthEmpRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "s_emp_record_id", type = IdType.AUTO)
    private Integer sEmpRecordId;

    @ApiModelProperty(value = "每月生成薪资id")
    private Integer sRecordId;

    @ApiModelProperty(value = "员工id")
    private Integer employeeId;

    @ApiModelProperty(value = "实际计薪时长")
    private BigDecimal actualWorkDay;

    @ApiModelProperty(value = "月计薪时长")
    private BigDecimal needWorkDay;

    @ApiModelProperty(value = "年")
    private Integer year;

    @ApiModelProperty(value = "月")
    private Integer month;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;




}
