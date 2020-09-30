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
 * 产品详情图片
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_crm_product_detail_img")
@ApiModel(value="CrmProductDetailImg对象", description="产品详情图片")
public class CrmProductDetailImg implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "img_id", type = IdType.AUTO)
    private Integer imgId;

    @ApiModelProperty(value = "产品id")
    private Integer productId;


    private String remarks;

    @ApiModelProperty(value = "主图")
    private String mainFileIds;

    private String detailFileIds;


}
