package com.kakarote.core.servlet;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.kakarote.core.utils.UserUtil;
import org.apache.ibatis.reflection.ExceptionUtil;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.MyBatisExceptionTranslator;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhangzhiwei
 * 通用serviceImpl
 */
public class BaseServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements BaseService<T> {


    @Override
    @Transactional(rollbackFor = Exception.class,isolation = Isolation.READ_COMMITTED)
    public boolean saveBatch(Collection<T> entityList) {
        return saveBatch(entityList, 1000);
    }

    @Override
    @Transactional(rollbackFor = Exception.class,isolation = Isolation.READ_COMMITTED)
    public boolean saveBatch(Collection<T> entityList, int batchSize) {
        if (CollUtil.isEmpty(entityList)) {
            return true;
        }
        insertFill(entityList);
        T model = entityList.iterator().next();
        Class<?> tClass = model.getClass();
        List<Field> allFields = TableInfoHelper.getAllFields(tClass);
        Set<String> fieldSet = allFields.stream().map(field -> StrUtil.toUnderlineCase(field.getName())).collect(Collectors.toSet());
        Map<String, Object> attrs = BeanUtil.beanToMap(model, true, true);
        int index = 0;
        StringBuilder columns = new StringBuilder();
        for (Map.Entry<String, Object> e : attrs.entrySet()) {
            if (fieldSet.contains(e.getKey())){
                if (index++ > 0) {
                    columns.append(',');
                }
                columns.append(e.getKey());
            }
        }
        StringBuilder sql = new StringBuilder();
        List<Object> parasNoUse = new ArrayList<>();
        TableInfo table = SqlHelper.table(tClass);
        forModelSave(table, attrs, sql, parasNoUse);
        int[] result = batch(tClass,sql.toString(), columns.toString(), entityList, batchSize);
        return result.length > 0;
    }

    /**
     * 字段填充
     */
    private void insertFill(Collection<T> entityList) {
        for (T model : entityList) {
            Map<String, Object> attrs = BeanUtil.beanToMap(model);
            for (String key : attrs.keySet()) {
                if (attrs.get(key) == null) {
                    switch (key) {
                        case BaseMetaObjectHandler.FIELD_CREATE_TIME:
                        case BaseMetaObjectHandler.FIELD_UPDATE_TIME:
                            attrs.put(key, DateUtil.formatDateTime(new Date()));
                            break;
                        case BaseMetaObjectHandler.FIELD_CREATE_USER:
                            attrs.put(key, UserUtil.getUserId());
                            break;
                        default:
                            break;
                    }
                }
            }
            BeanUtil.copyProperties(attrs, model);
        }
    }

    /**
     * 拼接sql
     */
    private void forModelSave(TableInfo table, Map<String, Object> attrs, StringBuilder sql, List<Object> paras) {
        sql.append("insert into `").append(table.getTableName()).append("`(");
        Set<String> columnTypeSet = table.getFieldList().stream().map(TableFieldInfo::getColumn).collect(Collectors.toSet());
        CollUtil.newArrayList(table.getAllSqlSelect().split(","));
        StringBuilder temp = new StringBuilder(") values(");
        for (Map.Entry<String, Object> e : attrs.entrySet()) {
            String colName = e.getKey();
            if (columnTypeSet.contains(colName)) {
                if (paras.size() > 0) {
                    sql.append(", ");
                    temp.append(", ");
                }
                sql.append('`').append(colName).append('`');
                temp.append('?');
                paras.add(e.getValue());
            }
        }
        sql.append(temp.toString()).append(')');
    }

    /**
     * 批量保存
     */
    private int[] batch(Class<?> tClass,String sql, String columns, Collection<T> entityList, int batchSize) {
        int[] batch;
        Connection conn = null;
        SqlSessionFactory sqlSessionFactory = SqlHelper.sqlSessionFactory(tClass);
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
        try {
            conn = sqlSession.getConnection();
            batch = batch(conn, sql, columns, new ArrayList<>(entityList), batchSize);
            sqlSession.commit();
        } catch (Throwable t) {
            t.printStackTrace();
            sqlSession.rollback();
            Throwable unwrapped = ExceptionUtil.unwrapThrowable(t);
            if (unwrapped instanceof RuntimeException) {
                MyBatisExceptionTranslator myBatisExceptionTranslator
                        = new MyBatisExceptionTranslator(sqlSessionFactory.getConfiguration().getEnvironment().getDataSource(), true);
                throw Objects.requireNonNull(myBatisExceptionTranslator.translateExceptionIfPossible((RuntimeException) unwrapped));
            }
            throw ExceptionUtils.mpe(unwrapped);
        } finally {
            sqlSession.close();
        }
        return batch;
    }

    private int[] batch(Connection conn, String sql, String columns, List<T> list, int batchSize) throws SQLException {
        if (list == null || list.size() == 0) {
            return new int[0];
        }
        if (batchSize < 1) {
            throw new IllegalArgumentException("The batchSize must more than 0.");
        }
        String[] columnArray = columns.split(",");
        for (int i = 0; i < columnArray.length; i++) {
            columnArray[i] = columnArray[i].trim();
        }
        int counter = 0;
        int pointer = 0;
        int size = list.size();
        int[] result = new int[size];
        PreparedStatement pst = conn.prepareStatement(sql);
        for (int i = 0; i < size; i++) {
            Map map = BeanUtil.beanToMap(list.get(i), true, true);
            for (int j = 0; j < columnArray.length; j++) {
                Object value = map.get(columnArray[j]);
                if (value instanceof java.util.Date) {
                    if (value instanceof java.sql.Date) {
                        pst.setDate(j + 1, (java.sql.Date) value);
                    } else if (value instanceof java.sql.Timestamp) {
                        pst.setTimestamp(j + 1, (java.sql.Timestamp) value);
                    } else {
                        java.util.Date d = (java.util.Date) value;
                        pst.setTimestamp(j + 1, new java.sql.Timestamp(d.getTime()));
                    }
                } else {
                    pst.setObject(j + 1, value);
                }
            }
            pst.addBatch();
            if (++counter >= batchSize) {
                counter = 0;
                int[] r = pst.executeBatch();
                for (int k = 0; k < r.length; k++) {
                    result[pointer++] = r[k];
                }
            }
        }
        if (counter != 0) {
            int[] r = pst.executeBatch();
            for (int k = 0; k < r.length; k++) {
                result[pointer++] = r[k];
            }
        }
        try {
            pst.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
