package com.kakarote.oa.entity.PO;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 日志关联销售简报表
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-07-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_oa_log_record")
@ApiModel(value="OaLogRecord对象", description="日志关联销售简报表")
public class OaLogRecord implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer logId;

    @ApiModelProperty(value = "客户数量")
    private Integer customerNum;

    @ApiModelProperty(value = "商机数量")
    private Integer businessNum;

    @ApiModelProperty(value = "合同数量")
    private Integer contractNum;

    @ApiModelProperty(value = "回款金额")
    private BigDecimal receivablesMoney;

    @ApiModelProperty(value = "跟进记录")
    private Integer activityNum;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;



}
