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
public class SimpleHrmDeptVO implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "部门id")
    private Integer deptId;

    @ApiModelProperty(value = "部门姓名")
    private String deptName;


}
