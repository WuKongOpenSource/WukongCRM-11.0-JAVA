package com.kakarote.work.entity.PO;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.kakarote.work.entity.VO.TaskInfoVO;
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
 * 任务表
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-06-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_work_task")
@ApiModel(value="WorkTask对象", description="任务表")
public class WorkTask implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "任务表")
    @TableId(value = "task_id", type = IdType.AUTO)
    private Integer taskId;

    @ApiModelProperty(value = "任务名称")
    private String name;

    @ApiModelProperty(value = "创建人ID")
    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @ApiModelProperty(value = "负责人ID")
    private Long mainUserId;

    @ApiModelProperty(value = "团队成员ID")
    private String ownerUserId;

    @ApiModelProperty(value = "新建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "完成状态 1正在进行2延期3归档 5结束")
    private Integer status;

    @ApiModelProperty(value = "分类id")
    private Integer classId;

    @ApiModelProperty(value = "标签 ,号拼接")
    private String labelId;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "上级ID")
    private Integer pid;

    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date stopTime;

    @ApiModelProperty(value = "优先级 从大到小 3高 2中 1低 0无")
    private Integer priority;

    @ApiModelProperty(value = "项目ID")
    private Integer workId;

    @ApiModelProperty(value = "工作台展示 0收件箱 1今天要做，2下一步要做，3以后要做")
    private Integer isTop;

    @ApiModelProperty(value = "是否公开")
    private Integer isOpen;

    @ApiModelProperty(value = "排序ID")
    private Integer orderNum;

    @ApiModelProperty(value = "我的任务排序ID")
    private Integer topOrderNum;

    @ApiModelProperty(value = "归档时间")
    private Date archiveTime;

    @ApiModelProperty(value = "是否删除 0 未删除 1 删除")
    private Integer ishidden;

    @ApiModelProperty(value = "删除时间")
    private Date hiddenTime;

    @ApiModelProperty(value = "批次")
    private String batchId;

    @ApiModelProperty(value = "1归档")
    private Integer isArchive;


    @TableField(exist = false)
    private String customerIds;
    @TableField(exist = false)
    private String contactsIds;
    @TableField(exist = false)
    private String businessIds;
    @TableField(exist = false)
    private String contractIds;

    @TableField(exist = false)
    private List<TaskInfoVO> taskInfoList;


}
