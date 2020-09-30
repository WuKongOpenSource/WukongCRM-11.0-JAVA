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
 * 公海表
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_crm_customer_pool")
@ApiModel(value="CrmCustomerPool对象", description="公海表")
public class CrmCustomerPool implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "公海id")
    @TableId(value = "pool_id", type = IdType.AUTO)
    private Integer poolId;

    @ApiModelProperty(value = "公海名称")
    private String poolName;

    @ApiModelProperty(value = "管理员 “,”分割")
    private String adminUserId;

    @ApiModelProperty(value = "公海规则员工成员 “,”分割")
    private String memberUserId;

    @ApiModelProperty(value = "公海规则部门成员 “,”分割")
    private String memberDeptId;

    @ApiModelProperty(value = "状态 0 停用 1启用")
    private Integer status;

    @ApiModelProperty(value = "前负责人领取规则 0不限制 1限制")
    private Integer preOwnerSetting;

    @ApiModelProperty(value = "前负责人领取规则限制天数")
    private Integer preOwnerSettingDay;

    @ApiModelProperty(value = "是否限制领取频率 0不限制 1限制")
    private Integer receiveSetting;

    @ApiModelProperty(value = "领取频率规则")
    private Integer receiveNum;

    @ApiModelProperty(value = "是否设置提前提醒 0不开启 1开启")
    private Integer remindSetting;

    @ApiModelProperty(value = "提醒规则天数")
    private Integer remindDay;

    @ApiModelProperty(value = "收回规则 0不自动收回 1自动收回")
    private Integer putInRule;

    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;



}
