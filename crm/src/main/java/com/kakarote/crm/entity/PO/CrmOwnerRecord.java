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
 * 负责人变更记录表
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-08-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_crm_owner_record")
@ApiModel(value="CrmOwnerRecord对象", description="负责人变更记录表")
public class CrmOwnerRecord implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "record_id", type = IdType.AUTO)
    private Integer recordId;

    @ApiModelProperty(value = "对象id")
    private Integer typeId;

    @ApiModelProperty(value = "对象类型")
    private Integer type;

    @ApiModelProperty(value = "上一负责人")
    private Long preOwnerUserId;

    @ApiModelProperty(value = "接手负责人")
    private Long postOwnerUserId;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;



}
