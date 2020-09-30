package com.kakarote.admin.entity.VO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zhangzhiwei
 */
@Data
public class UserBookVO implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty("姓名")
    private String realname;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty(value = "用户ID", required = true)
    private Long userId;

    @ApiModelProperty(value = "性别，0 未选择 1、男 2、女", required = true, allowableValues = "0,1,2")
    private Integer sex;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "部门ID")
    private Integer deptId;

    @ApiModelProperty(value = "部门名称")
    private String deptName;

    @ApiModelProperty(value = "关注状态,0未关注,1已关注")
    private Integer status;

    @ApiModelProperty(value = "状态,0禁用,1正常,2未激活")
    private Integer userStatus;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "岗位")
    private String post;

    @ApiModelProperty(value = "上级ID")
    private Long parentId;

    @ApiModelProperty(value = "上级名称")
    private String parentName;

    @ApiModelProperty(value = "角色ID")
    private String roleId;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty("首字母")
    private String initial;
}
