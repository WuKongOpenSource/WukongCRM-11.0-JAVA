package com.kakarote.hrm.entity.PO;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 员工合同
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_hrm_employee_contract")
@ApiModel(value="HrmEmployeeContract对象", description="员工合同")
public class HrmEmployeeContract implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "contract_id", type = IdType.AUTO)
    private Integer contractId;

    @ApiModelProperty("员工id")
    private Integer employeeId;

    @ApiModelProperty(value = "合同编号")
    private String contractNum;

    @ApiModelProperty(value = "1、固定期限劳动合同 2、无固定期限劳动合同 3、已完成一定工作任务为期限的劳动合同 4、实习协议 5、劳务合同 6、返聘协议 7、劳务派遣合同 8、借调合同 9、其他")
    private Integer contractType;

    @ApiModelProperty("合同开始时间")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    private Date startTime;

    @ApiModelProperty("合同结束日期")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    private Date endTime;

    @ApiModelProperty(value = "期限")
    private Integer term;

    @ApiModelProperty(value = "合同状态  0未执行 1 执行中、 2已到期、 ")
    private Integer status;

    @ApiModelProperty(value = "签约公司")
    private String signCompany;

    @ApiModelProperty(value = "合同签订日期")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    private Date signTime;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "是否到期提醒 0 否 1 是")
    private Integer isExpireRemind;

    @ApiModelProperty("排序")
    private Integer sort;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    private String batchId;




}
