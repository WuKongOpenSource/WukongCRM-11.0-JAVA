package com.kakarote.admin.entity.VO;

import com.kakarote.admin.entity.PO.AdminRole;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author zhangzhiwei
 * 查询角色功能
 */
@Data
@ToString
@ApiModel("查询角色功能")
public class AdminRoleVO {

    @ApiModelProperty("pid")
    private Integer pid;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("角色列表")
    private List<AdminRole> list;
}
