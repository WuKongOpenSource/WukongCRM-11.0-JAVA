package com.kakarote.hrm.entity.PO;

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
 * 绩效考核项模板
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_hrm_achievement_seg")
@ApiModel(value="HrmAchievementSeg对象", description="绩效考核项模板")
public class HrmAchievementSeg implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "seg_id", type = IdType.AUTO)
    private Integer segId;

    private Integer tableId;

    @ApiModelProperty(value = "考核项名称")
    private String segName;

    @ApiModelProperty(value = "是否固定 1 是 0 否")
    private Integer isFixed;

    @ApiModelProperty(value = "权重 -1 员工写权重比 0~100")
    private BigDecimal weight;

    @ApiModelProperty(value = "排序(前端排)")
    private Integer sort;



    @ApiModelProperty("考核子项")
    @TableField(exist = false)
    private List<HrmAchievementSegItem> items;




}
