package com.kakarote.oa.entity.PO;

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
 * 日程关联业务表
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-07-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_oa_event_relation")
@ApiModel(value="OaEventRelation对象", description="日程关联业务表")
public class OaEventRelation implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "日程关联业务表")
    @TableId(value = "eventrelation_id", type = IdType.AUTO)
    private Integer eventrelationId;

    @ApiModelProperty(value = "日程ID")
    private Integer eventId;

    @ApiModelProperty(value = "客户IDs")
    private String customerIds;

    @ApiModelProperty(value = "联系人IDs")
    private String contactsIds;

    @ApiModelProperty(value = "商机IDs")
    private String businessIds;

    @ApiModelProperty(value = "合同IDs")
    private String contractIds;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;



}
