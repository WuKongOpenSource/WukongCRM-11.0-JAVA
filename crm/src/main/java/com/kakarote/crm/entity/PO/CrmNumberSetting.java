package com.kakarote.crm.entity.PO;

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
 * 系统自动生成编号设置表
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_crm_number_setting")
@ApiModel(value="CrmNumberSetting对象", description="系统自动生成编号设置表")
public class CrmNumberSetting implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "设置id")
    @TableId(value = "setting_id", type = IdType.AUTO)
    private Integer settingId;

    @ApiModelProperty(value = "父级设置id")
    private Integer pid;

    @ApiModelProperty(value = "编号顺序")
    private Integer sort;

    @ApiModelProperty(value = "编号类型 1文本 2日期 3数字")
    private Integer type;

    @ApiModelProperty(value = "文本内容或日期格式或起始编号")
    private String value;

    @ApiModelProperty(value = "递增数")
    private Integer increaseNumber;

    @ApiModelProperty(value = "重新编号周期 1每天 2每月 3每年 4从不")
    private Integer resetType;

    @ApiModelProperty(value = "上次生成的编号")
    private Integer lastNumber;

    @ApiModelProperty(value = "上次生成的时间")
    private Date lastDate;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "创建人id")
    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;


}
