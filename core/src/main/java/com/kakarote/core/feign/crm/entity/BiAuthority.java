package com.kakarote.core.feign.crm.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 当前用户可查询对象
 * @author zhangzhiwei
 */
@ApiModel("用户可查询对象")
@Data
public class BiAuthority {

    @ApiModelProperty("用户列表")
    private List<Long> userIds;

    @ApiModelProperty("部门列表")
    private List<Integer> deptIds;
}
