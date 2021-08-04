package com.kakarote.hrm.entity.VO;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangeSalaryOptionVO {
    private String name;

    private Integer code;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String value;
}
