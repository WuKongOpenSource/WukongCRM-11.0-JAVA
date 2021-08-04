package com.kakarote.hrm.entity.BO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class QueryNotesByTimeBO {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date time;
}
