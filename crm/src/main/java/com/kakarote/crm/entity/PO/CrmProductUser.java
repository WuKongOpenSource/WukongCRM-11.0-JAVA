package com.kakarote.crm.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 产品员工小程序显示关联表
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_crm_product_user")
@ApiModel(value="CrmProductUser对象", description="产品员工小程序显示关联表")
public class CrmProductUser implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "product_user_id", type = IdType.AUTO)
    private Long productUserId;

    private String productIds;

    private Long userId;



}
