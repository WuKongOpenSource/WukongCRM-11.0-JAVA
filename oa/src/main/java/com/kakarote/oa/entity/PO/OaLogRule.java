package com.kakarote.oa.entity.PO;

import com.baomidou.mybatisplus.annotation.*;
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
 * 
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-07-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_oa_log_rule")
@ApiModel(value="OaLogRule对象", description="")
public class OaLogRule implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "rule_id", type = IdType.AUTO)
    private Integer ruleId;

    @ApiModelProperty(value = "状态 0停用 1启用")
    private Integer status;

    @ApiModelProperty(value = "需要提交的员工id，“,”分割")
    private String memberUserId;

    @ApiModelProperty(value = "日志类型 1日报 2周报 3月报")
    private Integer type;

    @ApiModelProperty(value = "每周需要统计的日期 1-6是周一到周六 7是周日")
    private String effectiveDay;

    @ApiModelProperty(value = "开始日期")
    private Integer startDay;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束日期")
    private Integer endDay;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(exist = false)
    private List<SimpleUser> memberUser;


}
