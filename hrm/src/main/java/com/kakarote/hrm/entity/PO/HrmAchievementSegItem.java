package com.kakarote.hrm.entity.PO;

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
 * 考核项选项
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_hrm_achievement_seg_item")
@ApiModel(value="HrmAchievementSegItem对象", description="考核项选项")
public class HrmAchievementSegItem implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "item_id", type = IdType.AUTO)
    private Integer itemId;

    private Integer segId;

    @ApiModelProperty(value = "选项名称")
    private String itemName;

    private Integer sort;




}
