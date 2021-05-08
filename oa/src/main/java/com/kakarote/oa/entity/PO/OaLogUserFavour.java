package com.kakarote.oa.entity.PO;

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
 * 用户点赞日志关系表 
 * </p>
 *
 * @author JiaS
 * @since 2021-03-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_oa_log_user_favour")
@ApiModel(value="OaLogUserFavour对象", description="用户点赞日志关系表 ")
public class OaLogUserFavour implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "日志id")
    private Integer logId;
}
