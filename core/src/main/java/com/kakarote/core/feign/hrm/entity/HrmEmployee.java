package com.kakarote.core.feign.hrm.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 员工表
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_hrm_employee")
@ApiModel(value="HrmEmployee对象", description="员工表")
public class HrmEmployee implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "员工id")
    @TableId(value = "employee_id", type = IdType.AUTO)
    private Integer employeeId;

    @ApiModelProperty(value = "员工姓名")
    private String employeeName;

    @ApiModelProperty(value = "手机")
    private String mobile;

    @ApiModelProperty(value = "国家地区")
    private String country;

    @ApiModelProperty(value = "民族")
    private String nation;

    @ApiModelProperty(value = "证件类型 1 身份证 2 港澳通行证 3 台湾通行证 4 护照 5 其他")
    private Integer idType;

    @ApiModelProperty(value = "证件号码")
    private String idNumber;

    @ApiModelProperty(value = "性别 1 男 2 女")
    private Integer sex;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "籍贯")
    private String nativePlace;

    @ApiModelProperty(value = "出生日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dateOfBirth;

    @ApiModelProperty(value = "生日类型 1 阳历 2 农历")
    private Integer birthdayType;

    @ApiModelProperty(value = "生日 示例：0323")
    private String birthday;

    @ApiModelProperty(value = "年龄")
    private Integer age;

    @ApiModelProperty(value = "户籍地址")
    private String address;

    @ApiModelProperty(value = "最高学历")
    private Integer highestEducation;

    @ApiModelProperty(value = "入职时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date entryTime;

    @ApiModelProperty(value = "试用期 0 无试用期")
    private Integer probation;

    @ApiModelProperty(value = "转正日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date becomeTime;

    @ApiModelProperty(value = "工号")
    private String jobNumber;

    @ApiModelProperty(value = "部门ID")
    private Integer deptId;

    @ApiModelProperty(value = "直属上级ID")
    private Integer parentId;

    @ApiModelProperty(value = "职位")
    private String post;

    @ApiModelProperty(value = "岗位职级")
    private String postLevel;

    @ApiModelProperty(value = "工作地点")
    private String workAddress;

    @ApiModelProperty(value = "工作详细地址")
    private String workDetailAddress;

    @ApiModelProperty(value = "工作城市")
    private String workCity;

    @ApiModelProperty("招聘渠道")
    private Integer channelId;

    @ApiModelProperty(value = "聘用形式 1 正式 2 非正式")
    private Integer employmentForms;

    @ApiModelProperty(value = "员工状态 1正式 2试用  3实习 4兼职 5劳务 6顾问 7返聘 8外包")
    private Integer status;

    @ApiModelProperty(value = "司龄开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date companyAgeStartTime;

    @ApiModelProperty(value = "司龄")
    private Integer companyAge;

    @ApiModelProperty(value = "候选人id")
    private Integer candidateId;

    @ApiModelProperty("入职状态 1 在职 2 待入职 3 待离职 4 离职")
    private Integer entryStatus;

    @ApiModelProperty(value = "0 未删除 1 删除")
    @TableLogic(value = "0",delval = "1")
    private Integer isDel;

    @ApiModelProperty(value = "创建人id")
    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;

    @ApiModelProperty(value = "部门名称")
    @TableField(exist = false)
    private String deptName;


}
