package com.kakarote.examine.entity.PO;

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
 * 审批流程指定成员记录表
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-11-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_examine_flow_member")
@ApiModel(value="ExamineFlowMember对象", description="审批流程指定成员记录表")
public class ExamineFlowMember implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "审批流程ID")
    private Integer flowId;

    @ApiModelProperty(value = "审批人ID")
    private Long userId;

    @ApiModelProperty(value = "1 依次审批 2 会签 3 或签")
    private Integer type;

    @ApiModelProperty(value = "排序规则")
    private Integer sort;

    @ApiModelProperty(value = "批次ID")
    private String batchId;


}
