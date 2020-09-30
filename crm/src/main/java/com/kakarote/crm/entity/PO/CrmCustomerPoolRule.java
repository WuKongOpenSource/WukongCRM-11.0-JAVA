package com.kakarote.crm.entity.PO;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 公海收回规则表
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_crm_customer_pool_rule")
@ApiModel(value="CrmCustomerPoolRule对象", description="公海收回规则表")
public class CrmCustomerPoolRule implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "收回规则id")
    @TableId(value = "rule_id", type = IdType.AUTO)
    private Integer ruleId;

    @ApiModelProperty(value = "公海id")
    private Integer poolId;

    @ApiModelProperty(value = "收回规则判断类型 1跟进记录 2商机 3成交状态")
    private Integer type;

    @ApiModelProperty(value = "已成交客户是否进入公海 0不进入 1进入")
    private Integer dealHandle;

    @ApiModelProperty(value = "有商机客户是否进入公海 0不进入 1进入")
    private Integer businessHandle;

    @ApiModelProperty(value = "客户级别设置 1全部 2根据级别分别设置")
    private Integer customerLevelSetting;

    @ApiModelProperty(value = "客户级别 1全部")
    private String level;

    @ApiModelProperty(value = "设置ID")
    @TableField(exist = false)
    private List<JSONObject> levelSetting;

    @ApiModelProperty(value = "公海规则限制天数")
    private Integer limitDay;



}
