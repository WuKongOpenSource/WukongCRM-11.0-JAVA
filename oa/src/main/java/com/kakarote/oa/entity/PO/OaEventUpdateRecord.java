package com.kakarote.oa.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 日程表
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-07-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_oa_event_update_record")
@ApiModel(value="OaEventUpdateRecord对象", description="日程表")
public class OaEventUpdateRecord implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer eventId;

    @ApiModelProperty(value = "标题")
    private Long time;

    @ApiModelProperty(value = "1 删除本次 2 修改本次 3 修改本次及以后")
    private Integer status;

    private Integer newEventId;

    private String batchId;



}
