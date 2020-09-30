package com.kakarote.bi.mapper;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.oa.entity.ExamineVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface BiWorkMapper {

    public List<JSONObject> logStatistics(Map<String,Object> map);

    public List<JSONObject> queryExamineCategory();

    public List<JSONObject> examineStatistics(Map<String,Object> map);

    BasePage<ExamineVO> myInitiate(BasePage<JSONObject> page, @Param("sqlDateFormat") String sqlDateFormat,
                                   @Param("userId") Long userId, @Param("categoryId") String categoryId,
                                   @Param("beginTime") Integer beginTime, @Param("finalTime") Integer finalTime);

    JSONObject queryExamineCount(@Param("sqlDateFormat") String sqlDateFormat,
                                 @Param("userId") Long userId,@Param("categoryId") String categoryId,
                                 @Param("beginTime") Integer beginTime,@Param("finalTime") Integer finalTime);
}
