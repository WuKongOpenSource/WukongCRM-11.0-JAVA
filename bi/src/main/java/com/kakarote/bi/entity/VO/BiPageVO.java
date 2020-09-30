package com.kakarote.bi.entity.VO;

import com.kakarote.core.entity.BasePage;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author admin
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BiPageVO<T> extends BasePage<T> {

    private Object duration;

    private Object money;
}
