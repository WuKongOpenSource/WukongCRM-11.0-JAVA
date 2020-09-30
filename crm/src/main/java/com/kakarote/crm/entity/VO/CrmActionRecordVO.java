package com.kakarote.crm.entity.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="CrmActionRecordVO对象", description="字段操作记录表")
public class CrmActionRecordVO {
    private static final long serialVersionUID=1L;

    private Integer id;

    @ApiModelProperty(value = "操作人ID")
    private Long createUserId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "ip地址")
    private String ipAddress;

    @ApiModelProperty(value = "模块类型")
    private Integer types;

    @ApiModelProperty(value = "被操作对象ID")
    private Integer actionId;

    @ApiModelProperty(value = "对象")
    private String object;

    @ApiModelProperty(value = "行为")
    private Integer behavior;

    @ApiModelProperty(value = "内容")
    private Object content;

    @ApiModelProperty(value = "详情")
    private String detail;


    @ApiModelProperty(value = "昵称")
    private String realname;

    @ApiModelProperty(value = "头像")
    private String img;
}
