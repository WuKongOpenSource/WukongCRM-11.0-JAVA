package com.kakarote.oa.common.repetitionstrategy;


import com.kakarote.oa.constart.RepetitionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 重复策略处理器
 * @author hmb
 */
@Component
public class RepetitionFactory {

    @Autowired
    private Map<String,RepetitionStrategy> repetitionStrategyMap = new HashMap<>();

    public  RepetitionStrategy getRepetitionStrategy(Integer type){
        RepetitionType repetitionType = RepetitionType.valueOf(type);
        return repetitionStrategyMap.get(repetitionType.getName());
    }
}
