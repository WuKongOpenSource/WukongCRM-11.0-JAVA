package com.kakarote.crm.entity.BO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class EsUpdateFieldBO {

    private String conditionField;

    private String conditionValue;

    private String updateField;

    private String updateValue;

	private Map<String, String> conditions;

	public EsUpdateFieldBO(String conditionField, String conditionValue, String updateField, String updateValue) {
		this.conditionField = conditionField;
		this.conditionValue = conditionValue;
		this.updateField = updateField;
		this.updateValue = updateValue;
	}
}
