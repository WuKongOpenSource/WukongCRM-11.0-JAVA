package com.kakarote.core.feign.admin.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zhangzhiwei
 * 简单的用户对象
 */
@Data
@ApiModel("用户对象")
public class SimpleUser implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("头像")
    private String img;

    @ApiModelProperty("昵称")
    private String realname;

    @ApiModelProperty("部门ID")
    private Integer deptId;

    @ApiModelProperty("部门名称")
    private String deptName;

    public SimpleUser() {
    }

    public SimpleUser(Long userId, String img, String realname, Integer deptId, String deptName) {
        this.userId = userId;
        this.img = img;
        this.realname = realname;
        this.deptId = deptId;
        this.deptName = deptName;
    }
}
