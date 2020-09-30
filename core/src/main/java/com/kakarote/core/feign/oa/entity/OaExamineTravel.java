package com.kakarote.core.feign.oa.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.kakarote.core.servlet.upload.FileEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 差旅行程表
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-07-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_oa_examine_travel")
@ApiModel(value="OaExamineTravel对象", description="差旅行程表")
public class OaExamineTravel implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "travel_id", type = IdType.AUTO)
    private Integer travelId;

    @ApiModelProperty(value = "审批ID")
    private Integer examineId;

    @ApiModelProperty(value = "出发地")
    private String startAddress;

    @ApiModelProperty(value = "出发时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty(value = "目的地")
    private String endAddress;

    @ApiModelProperty(value = "到达时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty(value = "交通费")
    private BigDecimal traffic;

    @ApiModelProperty(value = "住宿费")
    private BigDecimal stay;

    @ApiModelProperty(value = "餐饮费")
    private BigDecimal diet;

    @ApiModelProperty(value = "其他费用")
    private BigDecimal other;

    @ApiModelProperty(value = "金额")
    private BigDecimal money;

    @ApiModelProperty(value = "交通工具")
    private String vehicle;

    @ApiModelProperty(value = "单程往返（单程、往返）")
    private String trip;

    @ApiModelProperty(value = "时长")
    private BigDecimal duration;

    @ApiModelProperty(value = "备注")
    private String description;

    @ApiModelProperty(value = "批次id")
    private String batchId;

    @TableField(exist = false)
    private List<FileEntity> img = new ArrayList<>();
    @TableField(exist = false)
    private List<FileEntity> file = new ArrayList<>();

}
