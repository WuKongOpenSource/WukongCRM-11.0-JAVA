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
 * 审批流程主管审批记录表
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-11-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_examine_flow_superior")
@ApiModel(value="ExamineFlowSuperior对象", description="审批流程主管审批记录表")
public class ExamineFlowSuperior implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "审核流程ID")
    private Integer flowId;

    @ApiModelProperty(value = "直属上级级别 1 代表直属上级 2 代表 直属上级的上级")
    private Integer parentLevel;

    @ApiModelProperty(value = "找不到上级时，是否由上一级上级代审批 0 否 1 是")
    private Integer type;

    @ApiModelProperty(value = "批次ID")
    private String batchId;


}
