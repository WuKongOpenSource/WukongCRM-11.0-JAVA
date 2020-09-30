package com.kakarote.crm.entity.BO;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author Administrator
 */
@Data
@Accessors(chain = true)
public class CrmExamineData {
    private Integer recordId;
    private Integer status;
    private List<Long> examineLogIdList;
}
