package com.kakarote.crm.entity.PO;

import com.baomidou.mybatisplus.annotation.*;
import com.kakarote.core.feign.admin.entity.SimpleUser;
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
 * 审批步骤表
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_crm_examine_step")
@ApiModel(value="CrmExamineStep对象", description="审批步骤表")
public class CrmExamineStep implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "step_id", type = IdType.AUTO)
    private Long stepId;

    @ApiModelProperty(value = "步骤类型1、负责人主管，2、指定用户（任意一人），3、指定用户（多人会签）,4、上一级审批人主管")
    private Integer stepType;

    @ApiModelProperty(value = "审批ID")
    private Integer examineId;

    @ApiModelProperty(value = "审批人ID (使用逗号隔开) ,1,2,")
    private String checkUserId;

    @ApiModelProperty(value = "排序")
    private Integer stepNum;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "备注")
    private String remarks;


    @TableField(exist = false)
    @ApiModelProperty("审核人")
    private List<SimpleUser> userList;


}
