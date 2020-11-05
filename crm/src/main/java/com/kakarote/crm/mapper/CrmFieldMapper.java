package com.kakarote.crm.mapper;

import com.kakarote.core.servlet.BaseMapper;
import com.kakarote.crm.entity.PO.CrmField;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 自定义字段表 Mapper 接口
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-19
 */
public interface CrmFieldMapper extends BaseMapper<CrmField> {

    /**
     * 验证固定字段是否存在
     * @param tableName 表名
     * @param fieldName 字段
     * @param value 值
     * @param batchId batchId
     * @param label 类型
     * @return num
     */
    public Integer verifyFixedField(@Param("tableName") String tableName, @Param("fieldName") String fieldName,
                                @Param("value") String value, @Param("batchId") String batchId,@Param("label") Integer label);

    /**
     * 验证自定义字段是否存在
     * @param tableName 表名
     * @param fieldId 字段Id
     * @param value 值
     * @param batchId batchId
     * @return num
     */
    public Integer verifyField(@Param("tableName") String tableName, @Param("fieldId") Integer fieldId,
                                    @Param("value") String value, @Param("batchId") String batchId);

    public List<Map<String,Object>> initData(Map<String,Object> map);

    Integer queryFieldDuplicateByNoFixed(@Param("name") String name,@Param("value") Object value);

    Integer queryCustomerFieldDuplicateByFixed(@Param("name") String name,@Param("value") Object value);

    Integer updateFieldName(@Param("name") String name,@Param("type") Integer type,@Param("count") int count);

    public Integer dataCheck(@Param("name")String name,@Param("label")Integer label,@Param("type")Integer type);

}
