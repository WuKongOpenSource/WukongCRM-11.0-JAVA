package com.kakarote.admin.entity.PO;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-06-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_admin_user")
@ApiModel(value="AdminUser对象", description="用户表")
public class AdminUser implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "安全符")
    private String salt;

    @ApiModelProperty(value = "头像")
    private String img;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "真实姓名")
    private String realname;

    @ApiModelProperty(value = "员工编号")
    private String num;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "0 未选择 1 男 2 女 ")
    private Integer sex;

    @ApiModelProperty(value = "部门")
    private Integer deptId;

    @ApiModelProperty(value = "部门名称")
    @TableField(exist = false)
    private String deptName;

    @ApiModelProperty(value = "岗位")
    private String post;

    @ApiModelProperty(value = "状态,0禁用,1正常,2未激活")
    private Integer status;

    @ApiModelProperty(value = "直属上级ID")
    private Long parentId;

    @ApiModelProperty(value = "最后登录时间")
    private Date lastLoginTime;

    @ApiModelProperty(value = "最后登录IP 注意兼容IPV6")
    private String lastLoginIp;


    @TableField(exist = false)
    private String companyName;


}
