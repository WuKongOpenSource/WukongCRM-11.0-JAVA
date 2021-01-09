package com.kakarote.core.feign.examine.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author JiaS
 * @date 2020/12/17
 */
@Data
public class ExamineGeneralBO {

    @ApiModelProperty("流程ID")
    private Integer flowId;

    @ApiModelProperty("用户列表")
    private List<Long> userList;

    @ApiModelProperty("部门列表")
    private List<Integer> deptList;

    @ApiModelProperty("角色列表")
    private List<Integer> roleList;
}
