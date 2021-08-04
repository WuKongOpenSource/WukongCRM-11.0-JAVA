package com.kakarote.crm.entity.BO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class EsUpdatePropertiesBO {

    private String idField;

    private String nameField;

    private List<String> indexs;

	private Map<String, String> conditions;

	public EsUpdatePropertiesBO(String idField, String nameField, List<String> indexs) {
		this.idField = idField;
		this.nameField = nameField;
		this.indexs = indexs;
	}
}
