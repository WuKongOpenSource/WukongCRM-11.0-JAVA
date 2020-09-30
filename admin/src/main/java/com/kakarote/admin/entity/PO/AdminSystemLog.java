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
 * 系统日志表
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-06-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_admin_system_log")
@ApiModel(value="AdminSystemLog对象", description="系统日志表")
public class AdminSystemLog implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "操作人id")
    @TableField(fill = FieldFill.INSERT)
    private Integer createUserId;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "IP地址")
    private String ipAddress;

    @ApiModelProperty(value = "模块 1企业首页 2应用管理 3员工和部门管理 4名片小程序管理 5角色权限管理 6审批流（合同/回款） 7审批流（办公） 8项目管理 9客户管理 10系统日志管理 11其他设置")
    private Integer types;

    @ApiModelProperty(value = "行为")
    private Integer behavior;

    @ApiModelProperty(value = "操作对象")
    private String object;

    @ApiModelProperty(value = "操作详情")
    private String detail;
}
