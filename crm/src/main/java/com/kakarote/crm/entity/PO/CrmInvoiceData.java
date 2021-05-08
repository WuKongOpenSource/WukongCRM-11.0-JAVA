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
 * 线索自定义字段存值表
 * </p>
 *
 * @author dingshichong
 * @since 2021-03-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_crm_invoice_data")
@ApiModel(value="CrmInvoiceData对象", description="发票自定义字段存值表")
public class CrmInvoiceData implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer fieldId;


    @ApiModelProperty(value = "字段名称")
    private String name;

    private String value;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    private String batchId;

}
