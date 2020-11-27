package com.kakarote.crm.entity.VO;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kakarote.crm.entity.PO.CrmExamineStep;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class CrmQueryAllExamineVO {

    private static final long serialVersionUID=1L;

    @TableId(value = "examine_id", type = IdType.AUTO)
    private Integer examineId;

    @ApiModelProperty(value = "1 合同 2 回款 3发票")
    private Integer categoryType;

    @ApiModelProperty(value = "审核类型 1 固定审批 2 授权审批")
    private Integer examineType;

    @ApiModelProperty(value = "审批流名称")
    private String name;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "部门ID（0为全部）")
    @JsonProperty("deptList")
    private Object deptIds;

    @ApiModelProperty(value = "员工ID")
    @JsonProperty("userList")
    private Object userIds;

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

    @ApiModelProperty(value = "状态 1 启用 0 禁用 2 删除")
    private Integer status;

    @ApiModelProperty(value = "流程说明")
    private String remarks;


    @ApiModelProperty("修改人")
    private String updateUserName;

    @ApiModelProperty("创建人")
    private String createUserName;

    @ApiModelProperty("审核步骤")
    private List<CrmExamineStep> stepList;

}
