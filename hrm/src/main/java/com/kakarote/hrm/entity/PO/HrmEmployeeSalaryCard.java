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
 * 员工薪资卡信息
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_hrm_employee_salary_card")
@ApiModel(value = "HrmEmployeeSalaryCard对象", description = "员工薪资卡信息")
public class HrmEmployeeSalaryCard implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "salary_card_id", type = IdType.AUTO)
    private Integer salaryCardId;

    private Integer employeeId;

    @ApiModelProperty(value = "工资卡卡号")
    private String salaryCardNum;

    @ApiModelProperty(value = "开户城市")
    private String accountOpeningCity;

    @ApiModelProperty(value = "银行名称")
    private String bankName;

    @ApiModelProperty(value = "工资卡开户行")
    private String openingBank;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;


}
