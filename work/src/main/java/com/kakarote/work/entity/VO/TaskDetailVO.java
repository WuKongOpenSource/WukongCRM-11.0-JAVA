package com.kakarote.work.entity.VO;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.feign.admin.entity.SimpleUser;
import com.kakarote.core.feign.crm.entity.SimpleCrmEntity;
import com.kakarote.core.servlet.upload.FileEntity;
import com.kakarote.work.entity.BO.WorkTaskLabelBO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author wyq
 */
@Data
@ApiModel("任务详情信息")
public class TaskDetailVO {
    @ApiModelProperty(value = "任务id")
    private Integer taskId;

    @ApiModelProperty(value = "任务名称")
    private String name;

    @ApiModelProperty(value = "创建人ID")
    private Long createUserId;

    @ApiModelProperty(value = "创建人")
    private SimpleUser createUser;

    @ApiModelProperty(value = "负责人ID")
    private Long mainUserId;

    @ApiModelProperty(value = "负责人")
    private SimpleUser mainUser;

    @ApiModelProperty(value = "团队成员ID")
    private String ownerUserId;

    @ApiModelProperty(value = "团队成员")
    private List<SimpleUser> ownerUserList;

    @ApiModelProperty(value = "新建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
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
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Object stopTime;

    @ApiModelProperty(value = "优先级 从大到小 3高 2中 1低 0无")
    private Integer priority;

    @ApiModelProperty(value = "项目ID")
    private Integer workId;

    @ApiModelProperty(value = "项目名称")
    private String workName;

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


    @ApiModelProperty(value = "子任务")
    private List<TaskDetailVO> childTask;

    @ApiModelProperty(value = "附件")
    private List<FileEntity> file;

    @ApiModelProperty(value = "客户列表")
    private List<SimpleCrmEntity> customerList = new ArrayList<>();

    @ApiModelProperty(value = "联系人列表")
    private List<SimpleCrmEntity> contactsList = new ArrayList<>();

    @ApiModelProperty(value = "商机列表")
    private List<SimpleCrmEntity> businessList = new ArrayList<>();

    @ApiModelProperty(value = "合同列表")
    private List<SimpleCrmEntity> contractList = new ArrayList<>();

    @ApiModelProperty(value = "标签列表")
    private List<WorkTaskLabelBO> labelList;

    private JSONObject authList;
}
