package com.kakarote.core.servlet;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.baomidou.mybatisplus.core.toolkit.GlobalConfigUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.kakarote.core.common.Const;
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
        return saveBatch(entityList, Const.BATCH_SAVE_SIZE);
    }


    /**
     * 批量保存，取的字段为任何一列值不为空的字段
     */
    @Override
    @Transactional(rollbackFor = Exception.class,isolation = Isolation.READ_COMMITTED)
    public boolean saveBatch(Collection<T> entityList, int batchSize) {
        if (CollUtil.isEmpty(entityList)) {
            return true;
        }
        //获取表以及字段信息
        T model = entityList.iterator().next();
        Class<?> tClass = model.getClass();
        TableInfo table = SqlHelper.table(tClass);
        List<Field> allFields = TableInfoHelper.getAllFields(tClass);
        SqlSessionFactory sessionFactory = GlobalConfigUtils.getGlobalConfig(table.getConfiguration()).getSqlSessionFactory();
        Set<String> fieldSet = allFields.stream().filter(field -> {
                            TableId tableId = field.getAnnotation(TableId.class);
                            return tableId == null || !Objects.equals(tableId.type(),IdType.AUTO);
                        })
                .map(field -> StrUtil.toUnderlineCase(field.getName())).collect(Collectors.toSet());
        List<Map<String, Object>> mapList = insertFill(entityList, table,fieldSet);
        StringBuilder sql = new StringBuilder();
        forModelSave(table, fieldSet, sql);
        int[] result = batch(sessionFactory,sql.toString(), StrUtil.join(Const.SEPARATOR,fieldSet), mapList, batchSize);
        return result.length > 0;
    }

    /**
     * 字段填充
     */
    private List<Map<String,Object>> insertFill(Collection<T> entityList,TableInfo tableInfo,Set<String> keySet) {
        final IdentifierGenerator identifierGenerator = GlobalConfigUtils.getGlobalConfig(tableInfo.getConfiguration()).getIdentifierGenerator();
        List<Map<String,Object>> mapList = new ArrayList<>(entityList.size());
        Set<String> existFieldSet = new HashSet<>(keySet.size());
        for (T model : entityList) {
            Map<String, Object> attrs = BeanUtil.beanToMap(model,true,false);
            Object obj = attrs.get(tableInfo.getKeyColumn());
            if(obj == null) {
                if (tableInfo.getIdType().getKey() == IdType.ASSIGN_ID.getKey()) {
                    if (Number.class.isAssignableFrom(tableInfo.getKeyType())) {
                        attrs.put(tableInfo.getKeyColumn(), identifierGenerator.nextId(model));
                    } else {
                        attrs.put(tableInfo.getKeyColumn(), identifierGenerator.nextId(model).toString());
                    }
                } else if (tableInfo.getIdType().getKey() == IdType.ASSIGN_UUID.getKey()) {
                    attrs.put(tableInfo.getKeyColumn(), identifierGenerator.nextUUID(model));
                }
            }
            for (String key : keySet) {
                if (attrs.get(key) == null) {
                    switch (key) {
                        case "create_time":
                        case "update_time":
                            attrs.put(key, DateUtil.formatDateTime(new Date()));
                            break;
                        case "create_user_id":
                            attrs.put(key, UserUtil.getUserId());
                            break;
                        default:
                            break;
                    }
                }
                if (attrs.get(key) != null) {
                    existFieldSet.add(key);
                }

            }
            mapList.add(attrs);
        }
        keySet.retainAll(existFieldSet);
        return mapList;
    }

    /**
     * 拼接sql
     */
    private void forModelSave(TableInfo table, Set<String> attrs, StringBuilder sql) {
        sql.append("insert into `").append(table.getTableName()).append("`(");
        CollUtil.newArrayList(table.getAllSqlSelect().split(","));
        StringBuilder temp = new StringBuilder(") values(");
        int index = 0;
        for (String key : attrs) {
            if (index++ != 0) {
                sql.append(", ");
                temp.append(", ");
            }
            sql.append('`').append(key).append('`');
            temp.append('?');
        }
        sql.append(temp.toString()).append(')');
    }

    /**
     * 批量保存
     */
    private int[] batch(SqlSessionFactory sqlSessionFactory,String sql, String columns, Collection<Map<String,Object>> entityList, int batchSize) {
        int[] batch;
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
        try {
            Connection conn = sqlSession.getConnection();
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

    private int[] batch(Connection conn, String sql, String columns, List<Map<String,Object>> list, int batchSize) throws SQLException {
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
        for (Map<String,Object> map : list) {
            for (int j = 0; j < columnArray.length; j++) {
                Object value = map.get(columnArray[j]);
                if (value instanceof Date) {
                    if (value instanceof java.sql.Date) {
                        pst.setDate(j + 1, (java.sql.Date) value);
                    } else if (value instanceof java.sql.Timestamp) {
                        pst.setTimestamp(j + 1, (java.sql.Timestamp) value);
                    } else {
                        Date d = (Date) value;
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
                for (int j : r) {
                    result[pointer++] = j;
                }
            }
        }
        if (counter != 0) {
            int[] r = pst.executeBatch();
            for (int i : r) {
                result[pointer++] = i;
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
