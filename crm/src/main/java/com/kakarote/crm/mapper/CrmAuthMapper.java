package com.kakarote.crm.mapper;

import org.apache.ibatis.annotations.Param;

public interface CrmAuthMapper {
    /**
     * 查询权限内数据数量
     * @param name name
     * @param conditions 条件
     * @return num
     */
    public Integer queryAuthNum(@Param("tableName") String name, @Param("conditions") String conditions);

    /**
     * 查询是否有权限查看
     * @param a a
     * @param b b
     * @param userId 用户ID
     * @return num
     */
    public Integer queryReadFollowRecord(@Param("a") Integer a, @Param("b") Integer b,@Param("userId") Long userId);
}
