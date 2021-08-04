package com.kakarote.hrm.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 自定义字段管理表
 * </p>
 *
 * @author guomenghao
 * @since 2021-04-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_hrm_employee_field_manage")
@ApiModel(value="HrmEmployeeFieldManage对象", description="自定义字段管理表")
public class HrmEmployeeFieldManage implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "入职状态 1 在职 2 待入职 ")
    private Integer entryStatus;

    @ApiModelProperty(value = "字段id")
    private Integer fieldId;

    @ApiModelProperty(value = "字段标识")
    private String fieldName;

    @ApiModelProperty(value = "字段名称")
    private String name;

    @ApiModelProperty(value = "是否管理员可见 0 否 1 是  2 禁用否 3 禁用是")
    private Integer isManageVisible;


}
