package com.kakarote.hrm.entity.BO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddFileBO {

    @ApiModelProperty(value = "员工id")
    private Integer employeeId;

    @ApiModelProperty(value = "admin模块附件id")
    private Long fileId;

    @ApiModelProperty(value = "1 员工基本资料 2 员工档案资料 3员工离职资料")
    private Integer type;

    @ApiModelProperty(value = "11、身份证原件 12、学历证明 13、个人证件照 14、身份证复印件 15、工资银行卡 16、社保卡 17、公积金卡 18、获奖证书 19、其他 21、劳动合同 22、入职简历 23、入职登记表 24、入职体检单 25、离职证明 26、转正申请表 27、其他 31、离职审批 32、离职证明 33 、其他 ")
    private Integer subType;
}
