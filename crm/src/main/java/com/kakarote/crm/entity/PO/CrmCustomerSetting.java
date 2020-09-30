package com.kakarote.crm.entity.PO;

import com.baomidou.mybatisplus.annotation.*;
import com.kakarote.core.feign.admin.entity.SimpleDept;
import com.kakarote.core.feign.admin.entity.SimpleUser;
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
 * 员工拥有以及锁定客户数限制
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_crm_customer_setting")
@ApiModel(value="CrmCustomerSetting对象", description="员工拥有以及锁定客户数限制")
public class CrmCustomerSetting implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "setting_id", type = IdType.AUTO)
    private Integer settingId;

    @ApiModelProperty(value = "规则名称")
    private String settingName;

    @ApiModelProperty(value = "可拥有客户数量")
    private Integer customerNum;

    @ApiModelProperty(value = "适用范围")
    @TableField(exist = false)
    private String range;

    @ApiModelProperty(value = "成交客户是否占用数量 0 不占用 1 占用")
    private Integer customerDeal;

    @ApiModelProperty(value = "类型 1 拥有客户数限制 2 锁定客户数限制")
    private Integer type;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @ApiModelProperty(value = "部门列表")
    @TableField(exist = false)
    private List<SimpleDept> deptIds;

    @ApiModelProperty(value = "用户列表")
    @TableField(exist = false)
    private List<SimpleUser> userIds;


}
