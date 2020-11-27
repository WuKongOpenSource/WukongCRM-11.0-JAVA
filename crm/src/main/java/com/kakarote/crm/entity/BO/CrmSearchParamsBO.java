package com.kakarote.crm.entity.BO;

import com.kakarote.core.feign.crm.entity.BiParams;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author JiaS
 * @date 2020/11/23
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CrmSearchParamsBO extends BiParams {
    private CrmSearchBO.Search entity;
}
