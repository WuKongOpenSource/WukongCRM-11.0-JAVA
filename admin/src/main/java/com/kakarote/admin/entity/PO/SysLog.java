package com.kakarote.admin.entity.PO;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 系统日志
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-11-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_sys_log")
@ApiModel(value="SysLog对象", description="系统日志")
public class SysLog implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    @JsonIgnore
    private Long id;

    @ApiModelProperty(value = "类名")
    private String className;

    @ApiModelProperty(value = "方法名")
    private String methodName;

    @ApiModelProperty(value = "参数")
    private String args;

    @ApiModelProperty(value = "模块名称")
    private String model;

    @ApiModelProperty(value = "子模块名称（线索，客户。。。）")
    private Integer subModelLabel;

    @ApiModelProperty(value = "子模块名称（线索，客户。。。）")
    private String subModel;

    @ApiModelProperty(value = "对象")
    private String object;

    @ApiModelProperty(value = "行为")
    private String behavior;

    @ApiModelProperty(value = "操作详情")
    private String detail;

    @ApiModelProperty(value = "ip地址")
    private String ipAddress;

    private Long userId;

    @ApiModelProperty(value = "操作人名称")
    private String realname;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;


}
