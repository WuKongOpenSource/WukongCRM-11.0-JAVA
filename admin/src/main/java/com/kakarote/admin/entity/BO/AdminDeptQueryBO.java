package com.kakarote.admin.entity.BO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.ToString;

/**
 * @author zhangzhiwei
 */
@Data
@ToString
@ApiModel(value="部门编辑对象", description="部门对象")
public class AdminDeptQueryBO {

    @ApiParam(name = "id", value = "父级ID", required = true, example = "0")
    private Integer id;

    @ApiModelProperty(value = "类型")
    private String type;
}
