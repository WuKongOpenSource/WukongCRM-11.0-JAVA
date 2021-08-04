package com.kakarote.hrm.entity.VO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NotesVO {

    private Integer notesId;

    @ApiModelProperty("1 备忘 2 生日 3 入职 4 转正 5 离职 6 招聘 7 考勤打卡")
    private Integer type;

    @ApiModelProperty("内容")
    private String content;

    @ApiModelProperty("类型id")
    private Integer typeId;
}
