package com.kakarote.hrm.entity.PO;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author hmb
 * @since 2020-11-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_hrm_salary_archives_option")
@ApiModel(value="HrmSalaryArchivesOption对象", description="")
public class HrmSalaryArchivesOption implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "员工id")
    private Integer employeeId;

    private Integer isPro;

    @ApiModelProperty(value = "薪资项code")
    private Integer code;

    @ApiModelProperty(value = "薪资项名称")
    private String name;

    @ApiModelProperty(value = "薪资")
    private String value;


}
