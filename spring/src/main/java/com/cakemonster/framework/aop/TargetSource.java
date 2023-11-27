package com.cakemonster.framework.aop;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * TargetSource
 *
 * @author cakemonster
 * @date 2023/11/28
 */
@Data
@AllArgsConstructor
public class TargetSource {

    /**
     * 被代理class
     */
    private Class<?> beanClass;

    /**
     * 被代理对象
     */
    private Object bean;
}
