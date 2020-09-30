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
 * @author Ian
 * @date 2020/8/27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_call_record")
@ApiModel(value="CallRecord对象", description="通话记录表")
public class CallRecord implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "call_record_id", type = IdType.AUTO)
    private Integer callRecordId;

    @ApiModelProperty(value = "电话号码")
    private String number;

    @ApiModelProperty(value = "开始振铃时间")
    private Date startTime;

    @ApiModelProperty(value = "接通时间")
    private Date answerTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "通话时长(秒)")
    private Integer talkTime;

    @ApiModelProperty(value = "摘机时长")
    private Integer dialTime;

    @ApiModelProperty(value = "通话状态 (0未振铃，1未接通，2接通，3呼入未接通)")
    private Integer state;

    @ApiModelProperty(value = "通话类型 (0呼出，1呼入)")
    private Integer type;

    @ApiModelProperty(value = "关联模块 leads，customer，contacts")
    private String model;

    @ApiModelProperty(value = "关联模块ID")
    private Integer modelId;

    @ApiModelProperty(value = "录音文件路径")
    private String filePath;

    @ApiModelProperty(value = "录音文件大小")
    private Integer size;

    @ApiModelProperty(value = "文件名称")
    private String fileName;

    @ApiModelProperty(value = "0：CRM服务器; 1：上传至阿里云")
    private Integer callUpload;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "创建人ID")
    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @ApiModelProperty(value = "负责人ID")
    private Long ownerUserId;

    @ApiModelProperty(value = "批次")
    private String batchId;
}
