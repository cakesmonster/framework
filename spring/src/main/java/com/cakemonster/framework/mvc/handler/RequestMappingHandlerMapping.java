package com.cakemonster.framework.mvc.handler;

import com.cakemonster.framework.ioc.anno.Controller;
import com.cakemonster.framework.ioc.anno.RequestMapping;
import com.cakemonster.framework.ioc.bean.BeanDefinition;
import com.cakemonster.framework.ioc.context.AnnotationConfigApplicationContext;
import com.cakemonster.framework.ioc.meta.AnnotationMetaData;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sun.tools.javac.util.StringUtils;
import lombok.Data;
import lombok.Getter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * RequestMappingHandlerMapping
 *
 * @author cakemonster
 * @date 2023/11/25
 */
@Getter
public class RequestMappingHandlerMapping {

    private final AnnotationConfigApplicationContext context;

    private final Map<String, HandlerMethod> registry = Maps.newConcurrentMap();

    public RequestMappingHandlerMapping(AnnotationConfigApplicationContext context) {
        this.context = context;
    }

    public void initHandlerMethods() {
        for (BeanDefinition beanDefinition : getCandidateBeanNames()) {
            processCandidateBean(beanDefinition);
        }
    }

    private List<BeanDefinition> getCandidateBeanNames() {
        return Lists.newArrayList(context.getRegister().values());
    }

    private void processCandidateBean(BeanDefinition beanDefinition) {
        // 1. 判断带不带Controller/RequestMapping注解
        Class<?> beanClass = beanDefinition.getBeanClass();
        if (!beanClass.isAnnotationPresent(Controller.class)) {
            return;
        }
        String baseMapping = "/";
        if (beanClass.isAnnotationPresent(RequestMapping.class)) {
            baseMapping = beanClass.getAnnotation(RequestMapping.class).value();
        }
        Method[] methods = beanClass.getMethods();
        for (Method method : methods) {
            if (!method.isAnnotationPresent(RequestMapping.class)) {
                continue;
            }
            RequestMapping annotation = method.getAnnotation(RequestMapping.class);
            String methodMapping = annotation.value();
            // TODO(hzq): valid mapping
            if (!"/".equals(baseMapping)) {
                methodMapping = baseMapping + methodMapping;
            }
            Pattern pattern = Pattern.compile(methodMapping);
            HandlerMethod handlerMethod = new HandlerMethod(beanDefinition.getBean(), method, pattern);
            Parameter[] parameters = method.getParameters();
            for (int j = 0; j < parameters.length; j++) {
                Parameter parameter = parameters[j];
                if (parameter.getType() == HttpServletRequest.class
                    || parameter.getType() == HttpServletResponse.class) {
                    handlerMethod.getParamIndexMapping().put(parameter.getType().getSimpleName(), j);
                } else {
                    handlerMethod.getParamIndexMapping().put(parameter.getName(), j);
                }
            }
            registry.put(methodMapping, handlerMethod);
        }
    }
}
