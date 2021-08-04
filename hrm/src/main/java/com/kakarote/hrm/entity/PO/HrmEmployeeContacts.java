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
 * 员工联系人
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_hrm_employee_contacts")
@ApiModel(value="HrmEmployeeContacts对象", description="员工联系人")
public class HrmEmployeeContacts implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "contacts_id", type = IdType.AUTO)
    private Integer contactsId;

    private Integer employeeId;

    @ApiModelProperty(value = "联系人名称")
    private String contactsName;

    @ApiModelProperty(value = "关系")
    private String relation;

    @ApiModelProperty(value = "联系人电话")
    private String contactsPhone;

    @ApiModelProperty(value = "联系人工作单位")
    private String contactsWorkUnit;

    @ApiModelProperty(value = "联系儿职务")
    private String contactsPost;

    @ApiModelProperty(value = "联系人地址")
    private String contactsAddress;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    private Integer sort;




}
