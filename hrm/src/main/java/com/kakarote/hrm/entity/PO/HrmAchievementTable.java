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
 * 绩效考核表模板
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_hrm_achievement_table")
@ApiModel(value="HrmAchievementTable对象", description="绩效考核表模板")
public class HrmAchievementTable implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "table_id", type = IdType.AUTO)
    private Integer tableId;

    @ApiModelProperty(value = "考核名称")
    private String tableName;

    @ApiModelProperty(value = "1 OKR模板 2 KPI模板")
    private Integer type;

    @ApiModelProperty(value = "考核表描述")
    private String description;

    @ApiModelProperty(value = " 1 使用 0 删除")
    private Integer status;

    @ApiModelProperty("是否员工填写权重 0 否 1 是")
    private Integer isEmpWeight;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;




}
