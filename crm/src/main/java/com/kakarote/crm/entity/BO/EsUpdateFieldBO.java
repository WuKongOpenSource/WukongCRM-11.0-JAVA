package com.kakarote.crm.entity.BO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EsUpdateFieldBO {

    private String conditionField;

    private String conditionValue;

    private String updateField;

    private String updateValue;
}
