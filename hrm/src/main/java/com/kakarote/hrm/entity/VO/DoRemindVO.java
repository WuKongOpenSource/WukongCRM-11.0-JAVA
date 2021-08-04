package com.kakarote.hrm.entity.VO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoRemindVO {

    @ApiModelProperty("待审核薪资")
    private Integer toSalaryExamine;
    @ApiModelProperty("待离职")
    private Integer toLeave;
    @ApiModelProperty("合同到期")
    private Integer toExpireContract;
    @ApiModelProperty("待转正")
    private Integer toCorrect;
    @ApiModelProperty("待入职")
    private Integer toIn;
    @ApiModelProperty("生日")
    private Integer toBirthday;

}
