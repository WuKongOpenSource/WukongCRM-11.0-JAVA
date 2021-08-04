package com.kakarote.hrm.entity.PO;

import com.baomidou.mybatisplus.annotation.*;
import com.kakarote.core.servlet.upload.FileEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 员工附件表
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_hrm_employee_file")
@ApiModel(value="HrmEmployeeFile对象", description="员工附件表")
public class HrmEmployeeFile implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "employee_file_id", type = IdType.AUTO)
    private Integer employeeFileId;

    @ApiModelProperty(value = "员工id")
    private Integer employeeId;

    @ApiModelProperty(value = "admin模块附件id")
    private String fileId;

    @ApiModelProperty(value = "1 员工基本资料 2 员工档案资料 3员工离职资料")
    private Integer type;

    @ApiModelProperty(value = "11、身份证原件 12、学历证明 13、个人证件照 14、身份证复印件 15、工资银行卡 16、社保卡 17、公积金卡 18、获奖证书 19、其他 21、劳动合同 22、入职简历 23、入职登记表 24、入职体检单 25、离职证明 26、转正申请表 27、其他 31、离职审批 32、离职证明 33 、其他 ")
    private Integer subType;

    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;




    @TableField(exist = false)
    @ApiModelProperty("附件")
    private FileEntity file;


}
