package com.kakarote.hrm.entity.VO;

import com.kakarote.hrm.entity.PO.HrmActionRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class QueryRecordListVO extends HrmActionRecord {

//    private AdminUser adminUser
    @ApiModelProperty("头像")
    private String img;

    @ApiModelProperty("真实姓名")
    private String realname;
}
