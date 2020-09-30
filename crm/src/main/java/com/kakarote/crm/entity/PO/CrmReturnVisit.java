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
 * 
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-07-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_crm_return_visit")
@ApiModel(value="CrmReturnVisit对象", description="")
public class CrmReturnVisit implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "回访id")
    @TableId(value = "visit_id", type = IdType.AUTO)
    private Integer visitId;

    @ApiModelProperty(value = "回访编号")
    private String visitNumber;

    @ApiModelProperty(value = "回访时间")
    private Date visitTime;

    @ApiModelProperty(value = "回访人id")
    @TableField(fill = FieldFill.INSERT)
    private Long ownerUserId;

    @ApiModelProperty(value = "客户id")
    private Integer customerId;

    @ApiModelProperty(value = "合同id")
    private Integer contractId;

    @ApiModelProperty(value = "联系人id")
    private Integer contactsId;

    @ApiModelProperty(value = "创建人id")
    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "批次id")
    private String batchId;



}
