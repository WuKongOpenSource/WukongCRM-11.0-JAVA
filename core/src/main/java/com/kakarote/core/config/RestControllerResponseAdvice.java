package com.kakarote.core.config;

import com.kakarote.core.common.Const;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author zhangzhiwei
 */
@RestControllerAdvice
public class RestControllerResponseAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof Result) {
            parse(((Result) body).getData(), Const.AUTH_DATA_RECURSION_NUM);
        }
        return body;
    }

    @SuppressWarnings("unchecked")
    private Object parse(Object data, int depth) {
        depth--;
        if (depth < 0) {
            return new Object();
        }
        if (data instanceof Map) {
            mapToJson((Map) data, depth);
        } else if (data instanceof Collection) {
            iteratorToJson((Collection) data, depth);
        } else if (data instanceof BasePage) {
            iteratorToJson(((BasePage) data).getRecords(), depth);
        }

        return data;
    }

    private void mapToJson(Map<Object,Object> map, int depth) {
        Set<Object> set=new HashSet<>(map.keySet());
        for (Object key : set) {
            if(key instanceof String){
                map.put(underlineToCame((String) key), parse(map.remove(key), depth));
            }
        }
    }

    private void iteratorToJson(Collection iter, int depth) {
        for (Object o : iter) {
            parse(o, depth);
        }
    }

    private String underlineToCame(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (c == '_') {
                if (i + 1 < len) {
                    if (param.charAt(i + 1) >= 0x0061 && param.charAt(i + 1) <= 0x007a) {
                        sb.append(Character.toUpperCase(param.charAt(++i)));
                    } else {
                        sb.append(c);
                    }
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
