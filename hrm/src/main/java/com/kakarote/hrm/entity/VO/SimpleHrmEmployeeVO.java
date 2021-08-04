package com.kakarote.hrm.entity.VO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 员工表
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
@Data
public class SimpleHrmEmployeeVO implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "员工id")
    private Integer employeeId;

    @ApiModelProperty(value = "员工姓名")
    private String employeeName;

    @ApiModelProperty(value = "部门名称")
    private String deptName;

    @ApiModelProperty("岗位")
    private String post;

    private String mobile;

    private String email;

    private Integer sex;

    @ApiModelProperty("1 在职 2 离职 3 删除")
    private Integer status;

    private Integer isDel;




}
