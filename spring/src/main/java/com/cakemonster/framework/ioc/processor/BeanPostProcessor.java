package com.cakemonster.framework.ioc.processor;

/**
 * BeanPostProcessor
 *
 * @author cakemonster
 * @date 2023/11/27
 */
public interface BeanPostProcessor {

    Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception;

    Object postProcessAfterInitialization(Object bean, String beanName) throws Exception;

}
