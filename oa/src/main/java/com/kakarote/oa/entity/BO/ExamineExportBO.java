package com.kakarote.oa.entity.BO;

import com.kakarote.core.entity.PageEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ExamineExportBO extends PageEntity {

    private Integer categoryId;

    private Integer queryType;

    private Integer checkStatus;

    private Date startTime;

    private Date enbTime;

    private Long createUserId;

}
