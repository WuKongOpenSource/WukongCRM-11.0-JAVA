package com.kakarote.oa.entity.BO;

import com.kakarote.oa.entity.PO.OaEvent;
import com.kakarote.oa.entity.PO.OaEventNotice;
import com.kakarote.oa.entity.PO.OaEventRelation;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SetEventBO {

    @ApiModelProperty("日程")
    private OaEvent event;

    @ApiModelProperty("关联业务")
    private OaEventRelation relation;

    @ApiModelProperty("日程通知")
    private List<OaEventNotice> notice;

    private Integer type;

    private Long time;

}
