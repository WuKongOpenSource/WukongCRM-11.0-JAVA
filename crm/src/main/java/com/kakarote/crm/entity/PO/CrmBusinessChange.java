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
 * 商机阶段变化表
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_crm_business_change")
@ApiModel(value="CrmBusinessChange对象", description="商机阶段变化表")
public class CrmBusinessChange implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "change_id", type = IdType.AUTO)
    private Integer changeId;

    @ApiModelProperty(value = "商机ID")
    private Integer businessId;

    @ApiModelProperty(value = "阶段ID")
    private Integer statusId;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;



}
