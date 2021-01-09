package com.kakarote.core.common.log;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class Content {

    private String subModel;

    private String object;

    private String detail;

    private BehaviorEnum behavior;

    public Content(String object, String detail) {
        this.object = object;
        this.detail = detail;
    }

    public Content(String subModel, String object, String detail) {
        this.subModel = subModel;
        this.object = object;
        this.detail = detail;
    }

    public Content(String object, String detail, BehaviorEnum behavior) {
        this.object = object;
        this.detail = detail;
        this.behavior = behavior;
    }
}
