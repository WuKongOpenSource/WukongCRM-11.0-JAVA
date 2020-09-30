package com.kakarote.crm.entity.BO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.elasticsearch.script.Script;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangzhiwei
 * crm通用搜索对象
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "高级筛选BO", description = "高级筛选表")
@ToString
public class CrmSearchBO extends PageEntity {

    @ApiModelProperty(value = "搜索条件")
    private String search;

    @ApiModelProperty(value = "公海ID")
    private Integer poolId;

    @ApiModelProperty(value = "场景ID")
    private Integer sceneId;
    @ApiModelProperty(value = "type")
    private Integer label;
    @ApiModelProperty(value = "排序字段")
    private String sortField;
    @ApiModelProperty(value = "排序字段 1 倒序 2 正序")
    private Integer order;
    @ApiModelProperty(value = "高级筛选列表")
    private List<Search> searchList = new ArrayList<>();

    @Data
    @ApiModel(value = "高级筛选子查询")
    @Accessors(chain = true)
    public static class Search {
        @ApiModelProperty(value = "名字")
        private String name;
        @ApiModelProperty(value = "格式")
        private String formType;

        @ApiModelProperty(value = "高级筛选列表")
        @JsonProperty("type")
        private FieldSearchEnum searchEnum;

        @ApiModelProperty(value = "脚本查询需要")
        private Script script;

        @ApiModelProperty(value = "值列表")
        private List<String> values = new ArrayList<>();

        public Search(String name, String formType, FieldSearchEnum searchEnum, List<String> values) {
            this.name = name;
            this.formType = formType;
            this.searchEnum = searchEnum;
            this.values = values;
        }

        public Search() {
        }
    }

    @ApiModel(value = "字段搜索枚举")
    public enum FieldSearchEnum {
        /**
         * 为空
         */
        IS_NULL(5),
        /**
         * 不为空
         */
        IS_NOT_NULL(6),
        /**
         * 日期时间筛选
         */
        DATE_TIME(12),
        /**
         * 日期筛选
         */
        DATE(11),
        /**
         * 包含
         */
        CONTAINS(3),
        /**
         * 等于
         */
        IS(1),
        /**
         * 不等于
         */
        IS_NOT(2),
        /**
         * 不包含
         */
        NOT_CONTAINS(4),
        /**
         * 大于
         */
        GT(7),
        /**
         * 大于等于
         */
        EGT(8),
        /**
         * 小于
         */
        LT(9),
        /**
         * 小于等于
         */
        ELT(10),
        /**
         * 通过id
         */
        ID(11),
        /**
         * 不存在
         */
        NULL(0),


        SCRIPT(-1);

        FieldSearchEnum(Integer type) {
            this.type = type;
        }

        @JsonCreator
        public static FieldSearchEnum create(String type) {
            for (FieldSearchEnum searchEnum : values()) {
                if (searchEnum.getType().toString().equals(type)) {
                    return searchEnum;
                }
            }
            return NULL;
        }

        private Integer type;

        public Integer getType() {
            return type;
        }

        @Override
        public String toString() {
            return type.toString() + ":" + name();
        }

        public String valueOf(Integer type) {
            return type.toString() + ":" + name();
        }
    }
}
