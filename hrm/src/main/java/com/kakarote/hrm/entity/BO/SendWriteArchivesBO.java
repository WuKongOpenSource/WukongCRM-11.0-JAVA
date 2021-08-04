package com.kakarote.hrm.entity.BO;

import lombok.Data;

import java.util.List;

@Data
public class SendWriteArchivesBO {
    private List<Long> userIds;

    private List<Integer> deptIds;
}
