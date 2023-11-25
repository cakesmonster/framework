package com.cakemonster.framework.ioc.context;

/**
 * ApplicationContext
 *
 * @author cakemonster
 * @date 2023/11/25
 */
public interface ApplicationContext {

    Object getBean(String beanName);
}
