package com.kakarote.hrm.entity.PO;

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
 * 人力资源配置表
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_hrm_config")
@ApiModel(value="HrmConfig对象", description="人力资源配置表")
public class HrmConfig implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "config_id", type = IdType.AUTO)
    private Integer configId;

    @ApiModelProperty(value = "配置类型 1 淘汰原因")
    private Integer type;

    @ApiModelProperty(value = "值")
    private String value;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;




}
