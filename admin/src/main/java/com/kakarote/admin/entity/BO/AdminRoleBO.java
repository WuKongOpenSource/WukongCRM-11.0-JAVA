package com.kakarote.admin.entity.BO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author JiaS
 * @date 2020/11/21
 */
@Data
public class AdminRoleBO {

    @ApiModelProperty("人员id列表")
    private List<Long> userIds;

    @ApiModelProperty("部门id列表")
    private List<Integer> deptIds;

    @ApiModelProperty("权限id列表")
    private List<Integer> roleIds;

}
