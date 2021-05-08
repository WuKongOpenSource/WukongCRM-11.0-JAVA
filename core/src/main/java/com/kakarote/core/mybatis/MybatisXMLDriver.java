package com.kakarote.core.mybatis;

import com.baomidou.mybatisplus.core.MybatisXMLLanguageDriver;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.parsing.PropertyParser;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.scripting.defaults.RawSqlSource;
import org.apache.ibatis.scripting.xmltags.DynamicSqlSource;
import org.apache.ibatis.scripting.xmltags.TextSqlNode;
import org.apache.ibatis.session.Configuration;
import org.springframework.stereotype.Component;

@Component
public class MybatisXMLDriver extends MybatisXMLLanguageDriver {

    @Override
    public SqlSource createSqlSource(Configuration configuration, XNode script, Class<?> parameterType) {
        MybatisXMLBuilder builder = new MybatisXMLBuilder(configuration, script, parameterType);
        return builder.parseScriptNode();
    }

    @Override
    public SqlSource createSqlSource(Configuration configuration, String script, Class<?> parameterType) {
        // issue #3
        if (script.startsWith("<script>")) {
            XPathParser parser = new XPathParser(script, false, configuration.getVariables(), new MybatisMapperEntityResolver());
            return createSqlSource(configuration, parser.evalNode("/script"), parameterType);
        } else {
            // issue #127
            script = PropertyParser.parse(script, configuration.getVariables());
            TextSqlNode textSqlNode = new TextSqlNode(script);
            if (textSqlNode.isDynamic()) {
                return new DynamicSqlSource(configuration, textSqlNode);
            } else {
                return new RawSqlSource(configuration, script, parameterType);
            }
        }
    }
}
