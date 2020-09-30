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
 * 打印记录表
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-07-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_crm_print_record")
@ApiModel(value="CrmPrintRecord对象", description="打印记录表")
public class CrmPrintRecord implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "记录id")
    @TableId(value = "record_id", type = IdType.AUTO)
    private Integer recordId;

    private Integer crmType;

    private Integer typeId;

    @ApiModelProperty(value = "模板id")
    private Integer templateId;

    @ApiModelProperty(value = "模板名称")
    @TableField(exist = false)
    private String templateName;

    @ApiModelProperty(value = "打印记录")
    private String recordContent;

    @ApiModelProperty(value = "创建人id")
    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "创建人名称")
    @TableField(exist = false)
    private String createUserName;



}
