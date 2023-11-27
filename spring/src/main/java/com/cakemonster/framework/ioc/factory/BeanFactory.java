package com.cakemonster.framework.ioc.factory;

/**
 * BeanFactory
 *
 * @author cakemonster
 * @date 2023/11/20
 */
public interface BeanFactory {
    Object getBean(String beanName) throws Exception;
}
