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
 * 营销数据表
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-08-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_crm_marketing_info")
@ApiModel(value="CrmMarketingInfo对象", description="营销数据表")
public class CrmMarketingInfo implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "r_id", type = IdType.AUTO)
    private Integer rId;

    @ApiModelProperty(value = "关联ID")
    private Integer marketingId;

    @ApiModelProperty(value = "0未同步  1同步成功  2同步失败")
    private Integer status;

    @ApiModelProperty(value = "营销内容填写字段内容")
    private String fieldInfo;

    @ApiModelProperty(value = "设备号")
    private String device;

    @ApiModelProperty(value = "关联ID")
    private Long ownerUserId;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;



}
