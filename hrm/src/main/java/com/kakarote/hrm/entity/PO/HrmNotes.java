package com.kakarote.hrm.entity.PO;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 备忘
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-07-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_hrm_notes")
@ApiModel(value="HrmNotes对象", description="备忘")
public class HrmNotes implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "notes_id", type = IdType.AUTO)
    private Integer notesId;

    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date reminderTime;

    private Integer employeeId;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;




}
