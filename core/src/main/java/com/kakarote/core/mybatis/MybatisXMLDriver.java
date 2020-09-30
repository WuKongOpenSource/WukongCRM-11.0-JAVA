package com.kakarote.core.mybatis;

import com.baomidou.mybatisplus.core.MybatisXMLLanguageDriver;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.session.Configuration;
import org.springframework.stereotype.Component;

@Component
public class MybatisXMLDriver extends MybatisXMLLanguageDriver {

    @Override
    public SqlSource createSqlSource(Configuration configuration, XNode script, Class<?> parameterType) {
        MybatisXMLBuilder builder = new MybatisXMLBuilder(configuration, script, parameterType);
        return builder.parseScriptNode();
    }
}
