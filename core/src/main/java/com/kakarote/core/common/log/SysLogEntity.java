package com.kakarote.core.common.log;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class SysLogEntity {

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
    private Date createTime;

}
