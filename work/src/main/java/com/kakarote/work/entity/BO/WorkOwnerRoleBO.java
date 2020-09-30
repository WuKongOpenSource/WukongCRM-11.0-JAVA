package com.kakarote.work.entity.BO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wyq
 */
@Data
@ApiModel("项目成员角色信息")
public class WorkOwnerRoleBO {
    @ApiModelProperty("成员id")
    private Long userId;

    @ApiModelProperty("成员姓名")
    private String realname;

    @ApiModelProperty("成员头像")
    private String img;

    @ApiModelProperty("角色id")
    private Integer roleId;

    @ApiModelProperty("角色名称")
    private String roleName;
}
