package com.kakarote.examine.mapper;

import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import com.kakarote.examine.entity.BO.ExaminePageBO;
import com.kakarote.examine.entity.PO.ExamineRecord;
import com.kakarote.examine.entity.PO.ExamineRecordLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 审核日志表 Mapper 接口
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-11-19
 */
public interface ExamineRecordLogMapper extends BaseMapper<ExamineRecordLog> {

    BasePage<ExamineRecord> selectRecordLogListByUser(BasePage<Object> parse, @Param("data") ExaminePageBO examinePageBO,
                                                        @Param("userId") Long userId, @Param("roleIds") List<Integer> roleIds);


    List<Integer> selectRecordTypeIdListByUser(@Param("data") ExaminePageBO examinePageBO,
                                                      @Param("userId") Long userId, @Param("roleIds") List<Integer> roleIds);
}
