package com.kakarote.bi.entity.VO;

import com.kakarote.core.feign.crm.entity.BiParams;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Ian
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BiParamVO extends BiParams {
    private String categoryId;
}
