package com.kakarote.core.feign.admin.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 系统登录日志表
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-06-03
 */
@Data
public class LoginLogEntity implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "操作人id")
    @TableField(fill = FieldFill.INSERT)
    private Long userId;

    @ApiModelProperty(value = "操作人")
    private String realname;

    @ApiModelProperty(value = "登录时间")
    private Date loginTime;

    @ApiModelProperty(value = "登录ip地址")
    private String ipAddress;

    @ApiModelProperty(value = "登录地点")
    private String loginAddress;

    @ApiModelProperty(value = "设备类型")
    private String deviceType;

    @ApiModelProperty(value = "终端内核")
    private String core;

    @ApiModelProperty(value = "平台")
    private String platform;

    @ApiModelProperty(value = "认证结果 1成功 2失败")
    private Integer authResult;

    @ApiModelProperty(value = "失败结果")
    private String failResult;


}
