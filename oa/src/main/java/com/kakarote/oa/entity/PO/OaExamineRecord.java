package com.kakarote.oa.entity.PO;

import com.baomidou.mybatisplus.annotation.*;
import com.kakarote.oa.entity.VO.ExamineLogUserVO;
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
 * 审核记录表
 * </p>
 *
 * @author wyq
 * @since 2020-05-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_oa_examine_record")
@ApiModel(value="OaExamineRecord对象", description="审核记录表")
public class OaExamineRecord implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "审核记录ID")
    @TableId(value = "record_id", type = IdType.AUTO)
    private Integer recordId;

    @ApiModelProperty(value = "审批ID")
    private Integer examineId;

    @ApiModelProperty(value = "当前进行的审批步骤ID")
    private Integer examineStepId;

    @ApiModelProperty(value = "审核状态 0 未审核 1 审核通过 2 审核拒绝 3 审核中 4 已撤回")
    private Integer examineStatus;

    @ApiModelProperty(value = "创建人")
    private Long createUser;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "审核备注")
    private String remarks;


    @TableField(exist = false)
    private List<ExamineLogUserVO> userList;


}
