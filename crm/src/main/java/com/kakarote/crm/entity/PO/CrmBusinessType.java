package com.kakarote.crm.entity.PO;

import com.baomidou.mybatisplus.annotation.*;
import com.kakarote.core.feign.admin.entity.SimpleDept;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 商机状态组类别
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_crm_business_type")
@ApiModel(value="CrmBusinessType对象", description="商机状态组类别")
public class CrmBusinessType implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "type_id", type = IdType.AUTO)
    private Integer typeId;

    @ApiModelProperty(value = "标识")
    private String name;

    @ApiModelProperty(value = "部门ID")
    private String deptIds;

    @ApiModelProperty(value = "部门列表")
    @TableField(exist = false)
    private List<SimpleDept> deptList;

    @ApiModelProperty(value = "创建人ID")
    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @ApiModelProperty(value = "创建人")
    @TableField(exist = false)
    private String createName;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "0禁用1启用2删除")
    private Integer status;


    @TableField(exist = false)
    @ApiModelProperty(value = "商机阶段列表")
    private List<CrmBusinessStatus> statusList;


}
