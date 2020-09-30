package com.kakarote.crm.entity.PO;

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
 * 仪表盘排序表
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-06-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_crm_instrument_sort")
@ApiModel(value = "CrmInstrumentSort对象", description = "仪表盘排序表")
public class CrmInstrumentSort implements Serializable {

    private static final long serialVersionUID = 1L;

    public CrmInstrumentSort() {
    }

    public CrmInstrumentSort(Integer modelId, Integer isHidden,Integer list) {
        this.modelId = modelId;
        this.isHidden = isHidden;
        this.list = list;
    }

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "模块id 1、合同金额目标及完成情况 2、数据汇总 3、回款金额目标及完成情况 4、业绩指标完成率 5、销售漏斗 6、遗忘提醒 7、排行榜")
    private Integer modelId;

    @ApiModelProperty(value = "列 1左侧 2右侧")
    private Integer list;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "是否隐藏 0显示 1隐藏")
    private Integer isHidden;



}
