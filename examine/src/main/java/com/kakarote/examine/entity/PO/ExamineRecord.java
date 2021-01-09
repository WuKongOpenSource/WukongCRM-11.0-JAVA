package com.kakarote.examine.entity.PO;

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
 * 审核记录表
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-11-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_examine_record")
@ApiModel(value="ExamineRecord对象", description="审核记录表")
public class ExamineRecord implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "审核记录ID")
    @TableId(value = "record_id", type = IdType.AUTO)
    private Integer recordId;

    @ApiModelProperty(value = "审核ID")
    private Long examineId;

    @ApiModelProperty(value = "业务类型")
    private Integer label;

    @ApiModelProperty(value = "流程ID")
    private Integer flowId;

    @ApiModelProperty(value = "关联业务主键ID")
    private Integer typeId;

    @ApiModelProperty(value = "审核状态 0待审核、1通过、2拒绝、3审核中 4:撤回 5 未提交 6 创建 7 已删除 8 作废")
    private Integer examineStatus;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "修改人")
    private Long updateUserId;


}
