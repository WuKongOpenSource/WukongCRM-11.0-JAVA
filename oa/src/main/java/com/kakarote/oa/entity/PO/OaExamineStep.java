package com.kakarote.oa.entity.PO;

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
 * @since 2020-07-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_oa_examine_step")
@ApiModel(value="OaExamineStep对象", description="审批步骤表")
public class OaExamineStep implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "step_id", type = IdType.AUTO)
    private Integer stepId;

    @ApiModelProperty(value = "步骤类型1、负责人主管，2、指定用户（任意一人），3、指定用户（多人会签）,4、上一级审批人主管")
    private Integer stepType;

    @ApiModelProperty(value = "审批ID")
    private Integer categoryId;

    @ApiModelProperty(value = "审批人ID (使用逗号隔开) ,1,2,")
    private String checkUserId;

    @ApiModelProperty(value = "排序")
    private Integer stepNum;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;


    @TableField(exist = false)
    private List<SimpleUser> userList;


}
