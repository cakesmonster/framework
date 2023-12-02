package com.cakemonster.framework.ioc.processor;

import com.cakemonster.framework.ioc.factory.AbstractBeanFactory;

/**
 * BeanFactoryPostProcessor
 *
 * @author cakemonster
 * @date 2023/12/3
 */
public interface BeanFactoryPostProcessor {

    void postProcessBeanDefinitionRegistry(AbstractBeanFactory beanFactory) throws Exception;
}
