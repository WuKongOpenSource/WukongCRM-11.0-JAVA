package com.kakarote.admin.entity.PO;

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
 * 通讯录用户关注表
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-06-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_admin_attention")
@ApiModel(value="AdminAttention对象", description="通讯录用户关注表")
public class AdminAttention implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "attention_id", type = IdType.AUTO)
    private Integer attentionId;

    @ApiModelProperty(value = "被关注人")
    private Long beUserId;

    @ApiModelProperty(value = "关注人")
    private Long attentionUserId;
}
