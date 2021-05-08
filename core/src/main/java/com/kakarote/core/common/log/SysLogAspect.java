package com.kakarote.core.common.log;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.kakarote.core.common.SubModelType;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.feign.admin.service.LogService;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.core.utils.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.annotation.Order;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 系统日志切面
 *
 * @author hmb
 */
@Slf4j
@Aspect
@Component
@Order(2)
public class SysLogAspect {

    @Value("${spring.application.name}")
    private String applicationName;

    /**
     * 用于SpEL表达式解析.
     */
    private SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
    /**
     * 用于获取方法参数定义名字.
     */
    private DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    @Pointcut("@within(sysLogHandler) || @annotation(sysLogHandler)")
    public void point(SysLogHandler sysLogHandler) {
    }

    private List<SysLogEntity> logEntityList;

    @SuppressWarnings("unchecked")
    @Before(value = "point(sysLogHandler)", argNames = "joinPoint,sysLogHandler")
    public void beforeMethod(JoinPoint joinPoint, SysLogHandler sysLogHandler) {
        SysLog sysLog = joinPoint.getTarget().getClass().getDeclaredAnnotation(SysLog.class);
        if (sysLogHandler == null) {
            return;
        }
        logEntityList = new ArrayList<>();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        SysLogEntity sysLogEntity = getLog(joinPoint);
        if (StrUtil.isNotEmpty(sysLogHandler.applicationName())) {
            sysLogEntity.setModel(sysLogHandler.applicationName());
        } else {
            sysLogEntity.setModel(applicationName);
        }
        if (sysLogHandler.subModel() != SubModelType.NULL) {
            sysLogEntity.setSubModel(sysLogHandler.subModel().getName());
            sysLogEntity.setSubModelLabel(sysLogHandler.subModel().getLabel());
        } else if (sysLog != null) {
            sysLogEntity.setSubModel(sysLog.subModel().getName());
            sysLogEntity.setSubModelLabel(sysLog.subModel().getLabel());
        }
        try {
            if (sysLogHandler.isReturn()) {
                logEntityList.add(sysLogEntity);
            } else {
                if (StrUtil.isNotEmpty(sysLogHandler.object()) && StrUtil.isNotEmpty(sysLogHandler.detail())) {
                    String object = sysLogHandler.object();
                    String detail = sysLogHandler.detail();
                    if (object.contains("#")) {
                        //获取方法参数值
                        Object[] args = joinPoint.getArgs();
                        object = getValBySpEL(object, methodSignature, args);
                    }
                    if (detail.contains("#")) {
                        //获取方法参数值
                        Object[] args = joinPoint.getArgs();
                        detail = getValBySpEL(detail, methodSignature, args);
                    }
                    sysLogEntity.setDetail(detail);
                    sysLogEntity.setObject(object);
                    sysLogEntity.setBehavior(sysLogHandler.behavior().getName());
                    logEntityList.add(sysLogEntity);
                } else {
                    Method method = methodSignature.getMethod();
                    Class proxyClass = sysLog.logClass();
                    Method logMethod = proxyClass.getDeclaredMethod(method.getName(), method.getParameterTypes());
                    Object object = proxyClass.newInstance();
                    Object logInvoke = logMethod.invoke(object, joinPoint.getArgs());
                    logEntityList.addAll(transferLogEntity(logInvoke, sysLogEntity, sysLogHandler));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("日志记录异常:{}", e.getMessage());
        }
    }

    @AfterReturning(value = "point(sysLogHandler)", argNames = "sysLogHandler,val", returning = "val")
    public void afterMethodReturning(SysLogHandler sysLogHandler, Object val) {
        if (sysLogHandler == null) {
            return;
        }
        if (sysLogHandler.isReturn()) {
            SysLogEntity sysLogEntity = logEntityList.get(0);
            logEntityList = new ArrayList<>();
            logEntityList.addAll(transferLogEntity(val, sysLogEntity, sysLogHandler));
        }
        try {
            for (SysLogEntity sysLogEntity : logEntityList) {
                ApplicationContextHolder.getBean(LogService.class).saveSysLog(sysLogEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("日志记录异常:{}", e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private List<SysLogEntity> transferLogEntity(Object obj, SysLogEntity sysLogEntity, SysLogHandler sysLogHandler) {
        List<SysLogEntity> sysLogEntityList = new ArrayList<>();
        if (obj instanceof Content) {
            Content content = (Content) obj;
            sysLogEntity.setDetail(content.getDetail());
            sysLogEntity.setObject(content.getObject());
            if (content.getBehavior() == null) {
                sysLogEntity.setBehavior(sysLogHandler.behavior().getName());
            } else {
                sysLogEntity.setBehavior(content.getBehavior().getName());
            }
            if (StrUtil.isNotEmpty(content.getSubModel())) {
                sysLogEntity.setSubModel(content.getSubModel());
            }
            sysLogEntityList.add(sysLogEntity);
        } else if (obj instanceof Collection) {
            List<Content> contentList = (List) obj;
            for (Content content : contentList) {
                SysLogEntity sysLogEntity1 = BeanUtil.copyProperties(sysLogEntity, SysLogEntity.class);
                sysLogEntity1.setDetail(content.getDetail());
                sysLogEntity1.setObject(content.getObject());
                if (content.getBehavior() == null) {
                    sysLogEntity1.setBehavior(sysLogHandler.behavior().getName());
                } else {
                    sysLogEntity1.setBehavior(content.getBehavior().getName());
                }
                if (StrUtil.isNotEmpty(content.getSubModel())) {
                    sysLogEntity.setSubModel(content.getSubModel());
                }
                sysLogEntityList.add(sysLogEntity1);
            }
        }
        return sysLogEntityList;
    }

    /**
     * 构建日志对象
     */
    private SysLogEntity getLog(JoinPoint joinPoint) {
        SysLogEntity sysLogEntity = new SysLogEntity();
        sysLogEntity.setCreateTime(new Date());
        Object[] args = joinPoint.getArgs();
        List<Object> objectList = new ArrayList<>(args.length);
        for (Object arg : args) {
            boolean parse = parse(arg);
            if(parse) {
                objectList.add(arg);
            }
        }
        sysLogEntity.setArgs(JSON.toJSONString(objectList, new JsonFilter()));
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        sysLogEntity.setClassName(methodSignature.getDeclaringTypeName());
        sysLogEntity.setMethodName(methodSignature.getName());
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String ipAddress = ServletUtil.getClientIP(request);
            sysLogEntity.setIpAddress(ipAddress);
        }
        UserInfo user = UserUtil.getUser();
        sysLogEntity.setUserId(user.getUserId());
        sysLogEntity.setRealname(user.getRealname());
        return sysLogEntity;
    }

    private class JsonFilter implements PropertyFilter {

        /**
         * @param object the owner of the property
         * @param name   the name of the property
         * @param value  the value of the property
         * @return true if the property will be included, false if to be filtered out
         */
        @Override
        public boolean apply(Object object, String name, Object value) {
            return parse(object) && parse(value);
        }
    }

    /**
     * 数据是否可以序列化
     * @param object
     * @return true
     */
    private boolean parse(Object object){
        if (object instanceof HttpServletRequest) {
            return false;
        } else if (object instanceof HttpServletResponse) {
            return false;
        } else if (object instanceof MultipartFile) {
            return false;
        } else if (object instanceof OutputStream) {
            return false;
        }
        return true;
    }
    /**
     * 解析spEL表达式
     */
    private String getValBySpEL(String spEL, MethodSignature methodSignature, Object[] args) {
        //获取方法形参名数组
        String[] paramNames = nameDiscoverer.getParameterNames(methodSignature.getMethod());
        if (paramNames != null && paramNames.length > 0) {
            Expression expression = spelExpressionParser.parseExpression(spEL);
            // spring的表达式上下文对象
            EvaluationContext context = new StandardEvaluationContext();
            // 给上下文赋值
            for (int i = 0; i < args.length; i++) {
                context.setVariable(paramNames[i], args[i]);
            }
            Object value = expression.getValue(context);
            return value!=null?value.toString():null;
        }
        return null;
    }
}
