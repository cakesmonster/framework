package com.cakemonster.framework.ioc.processor;

import com.cakemonster.framework.ioc.factory.AbstractBeanFactory;
import com.cakemonster.framework.ioc.meta.AnnotationMetaData;

/**
 * ImportBeanDefinitionRegistrar
 *
 * @author cakemonster
 * @date 2023/12/3
 */
public interface ImportBeanDefinitionRegistrar {
    default void registerBeanDefinitions(AnnotationMetaData metaData, AbstractBeanFactory beanFactory) {

    }
}
