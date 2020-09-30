package com.kakarote.work.entity.PO;

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
 * 任务关联业务表
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-06-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_work_task_relation")
@ApiModel(value="WorkTaskRelation对象", description="任务关联业务表")
public class WorkTaskRelation implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "任务关联业务表")
    @TableId(value = "r_id", type = IdType.AUTO)
    private Integer rId;

    @ApiModelProperty(value = "任务ID")
    private Integer taskId;

    @ApiModelProperty(value = "客户IDs")
    private String customerIds;

    @ApiModelProperty(value = "联系人IDs")
    private String contactsIds;

    @ApiModelProperty(value = "商机IDs")
    private String businessIds;

    @ApiModelProperty(value = "合同IDs")
    private String contractIds;

    @ApiModelProperty(value = "状态1可用")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;



}
