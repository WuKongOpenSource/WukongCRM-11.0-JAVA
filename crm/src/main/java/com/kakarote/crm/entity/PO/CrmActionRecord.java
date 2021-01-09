package com.kakarote.crm.entity.PO;

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
 * 字段操作记录表
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_crm_action_record")
@ApiModel(value="CrmActionRecord对象", description="字段操作记录表")
public class CrmActionRecord implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "操作人ID")
    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
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
    private String content;

    @ApiModelProperty(value = "详情")
    private String detail;



}
