package com.kakarote.work.entity.BO;

import com.kakarote.core.entity.PageEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueryTaskFileByWorkIdBO extends PageEntity {

    private Integer workId;
}
