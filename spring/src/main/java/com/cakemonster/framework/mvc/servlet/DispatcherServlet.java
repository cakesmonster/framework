package com.cakemonster.framework.mvc.servlet;

import com.cakemonster.framework.ioc.context.AnnotationConfigApplicationContext;
import com.cakemonster.framework.mvc.handler.HandlerMethod;
import com.cakemonster.framework.mvc.handler.RequestMappingHandlerMapping;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;

/**
 * DispatcherServlet
 *
 * @author cakemonster
 * @date 2023/11/25
 */
public class DispatcherServlet extends HttpServlet {

    private RequestMappingHandlerMapping handlerMapping;

    private Properties properties = new Properties();

    @Override
    public void init(ServletConfig config) throws ServletException {
        String contextConfigLocation = config.getInitParameter("contextConfigLocation");
        doLoadConfig(contextConfigLocation);
        String scanPackage = properties.getProperty("scanPackage");
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(scanPackage);
        handlerMapping = new RequestMappingHandlerMapping(context);
        handlerMapping.initHandlerMethods();
        System.out.println("mvc初始化完成...");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HandlerMethod handlerMethod = getHandlerMethod(req);
        if (handlerMethod == null) {
            resp.getWriter().write("404 not found");
            return;
        }
        // 参数绑定
        Class<?>[] parameterTypes = handlerMethod.getMethod().getParameterTypes();
        List<Object> paramValues = Lists.newArrayList();
        Map<String, String[]> parameterMap = req.getParameterMap();
        for (Map.Entry<String, String[]> param : parameterMap.entrySet()) {
            String paramValue = Joiner.on(",").join(param.getValue());
            if (!handlerMethod.getParamIndexMapping().containsKey(param.getKey())) {
                continue;
            }
            Integer index = handlerMethod.getParamIndexMapping().get(param.getKey());
            paramValues.add(index, paramValue);
        }
        Integer requestIndex = handlerMethod.getParamIndexMapping().get(HttpServletRequest.class.getSimpleName());
        if (requestIndex != null) {
            paramValues.add(requestIndex, req);
        }
        Integer responseIndex = handlerMethod.getParamIndexMapping().get(HttpServletResponse.class.getSimpleName());
        if (responseIndex != null) {
            paramValues.add(responseIndex, resp);
        }
        // 最终调用handler的method属性
        try {
            handlerMethod.getMethod().invoke(handlerMethod.getBean(), paramValues.toArray());
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new ServletException(e);
        }
    }

    private HandlerMethod getHandlerMethod(HttpServletRequest req) {
        Map<String, HandlerMethod> registry = handlerMapping.getRegistry();
        if (registry.isEmpty()) {
            return null;
        }
        String url = req.getRequestURI();
        for (HandlerMethod handlerMethod : registry.values()) {
            Matcher matcher = handlerMethod.getPattern().matcher(url);
            if (!matcher.matches()) {
                continue;
            }
            return handlerMethod;
        }
        return null;
    }

    private void doLoadConfig(String contextConfigLocation) {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(contextConfigLocation);
        try {
            properties.load(resourceAsStream);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

}
