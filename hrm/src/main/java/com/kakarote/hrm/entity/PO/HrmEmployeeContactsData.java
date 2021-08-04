package com.kakarote.hrm.entity.PO;

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
 * 客户扩展字段数据表
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_hrm_employee_contacts_data")
@ApiModel(value="HrmEmployeeContactsData对象", description="客户扩展字段数据表")
public class HrmEmployeeContactsData implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer fieldId;

    private Integer labelGroup;

    @ApiModelProperty(value = "字段名称")
    private String name;

    @ApiModelProperty(value = "字段值")
    private String fieldValue;

    @ApiModelProperty(value = "字段值描述")
    private String fieldValueDesc;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "contacts_id")
    private Integer contactsId;




    @ApiModelProperty(value = "字段类型")
    @TableField(exist = false)
    private Integer type;

}
