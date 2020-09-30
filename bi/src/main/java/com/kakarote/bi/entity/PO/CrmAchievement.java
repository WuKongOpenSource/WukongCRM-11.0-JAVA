package com.kakarote.bi.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 业绩目标
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-07-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_crm_achievement")
@ApiModel(value="CrmAchievement对象", description="业绩目标")
public class CrmAchievement implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "achievement_id", type = IdType.AUTO)
    private Integer achievementId;

    @ApiModelProperty(value = "对象ID")
    private Integer objId;

    @ApiModelProperty(value = "对象名称")
    @TableField(exist = false)
    private String objName;

    @ApiModelProperty(value = "对象ID数组")
    @TableField(exist = false)
    private List<Integer> objIds;

    @ApiModelProperty(value = "1公司2部门3员工")
    private Integer type;

    @ApiModelProperty(value = "年")
    private String year;

    @ApiModelProperty(value = "一月")
    private BigDecimal january;

    @ApiModelProperty(value = "二月")
    private BigDecimal february;

    @ApiModelProperty(value = "三月")
    private BigDecimal march;

    @ApiModelProperty(value = "四月")
    private BigDecimal april;

    @ApiModelProperty(value = "五月")
    private BigDecimal may;

    @ApiModelProperty(value = "六月")
    private BigDecimal june;

    @ApiModelProperty(value = "七月")
    private BigDecimal july;

    @ApiModelProperty(value = "八月")
    private BigDecimal august;

    @ApiModelProperty(value = "九月")
    private BigDecimal september;

    @ApiModelProperty(value = "十月")
    private BigDecimal october;

    @ApiModelProperty(value = "十一月")
    private BigDecimal november;

    @ApiModelProperty(value = "十二月")
    private BigDecimal december;

    @ApiModelProperty(value = "1销售（目标）2回款（目标）")
    private Integer status;

    @ApiModelProperty(value = "年目标")
    private BigDecimal yeartarget;



}
