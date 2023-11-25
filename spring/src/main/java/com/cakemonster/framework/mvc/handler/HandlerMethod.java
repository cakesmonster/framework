package com.cakemonster.framework.mvc.handler;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * HandlerMethod
 *
 * @author cakemonster
 * @date 2023/11/25
 */
@Data
@AllArgsConstructor
public class HandlerMethod {

    /**
     * bean实例
     */
    private Object bean;

    /**
     * 方法
     */
    private Method method;

    /**
     * url pattern
     */
    private Pattern pattern;

    /**
     * 参数顺序,是为了进行参数绑定，key是参数名，value代表是第几个参数 <name,2>
     */
    private Map<String, Integer> paramIndexMapping;

    public HandlerMethod(Object bean, Method method, Pattern pattern) {
        this.bean = bean;
        this.method = method;
        this.pattern = pattern;
        this.paramIndexMapping = Maps.newHashMap();
    }

}
