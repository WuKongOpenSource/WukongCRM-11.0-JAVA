package com.kakarote.crm.entity.PO;

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
 * 商机状态
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_crm_business_status")
@ApiModel(value="CrmBusinessStatus对象", description="商机状态")
public class CrmBusinessStatus implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "status_id", type = IdType.AUTO)
    private Integer statusId;

    @ApiModelProperty(value = "商机状态类别ID")
    private Integer typeId;

    @ApiModelProperty(value = "标识")
    private String name;

    @ApiModelProperty(value = "赢单率")
    private String rate;

    @ApiModelProperty(value = "排序")
    private Integer orderNum;



}
