package com.kakarote.oa.entity.VO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class ExamineLogUserVO {

    private Date examineTime;

    private Integer examineStatus;

    private String realname;

    private Long userId;

    private String img;

    private ExamineUserVO examineUser;

    @Builder
    public static class ExamineUserVO{
        private Date examineTime;

        private Integer examineStatus;

        private String realname;

        private Long userId;

        private String img;
    }
}

