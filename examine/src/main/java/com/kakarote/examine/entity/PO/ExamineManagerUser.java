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
 * 审批管理员设置表
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-11-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_examine_manager_user")
@ApiModel(value="ExamineManagerUser对象", description="审批管理员设置表")
public class ExamineManagerUser implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "审批ID")
    private Long examineId;

    @ApiModelProperty(value = "管理员ID")
    private Long userId;

    @ApiModelProperty(value = "从小到大")
    private Integer sort;


}
