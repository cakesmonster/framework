package com.cakemonster.framework.spring;

import com.cakemonster.framework.ioc.anno.FactoryBean;

/**
 * MapperBeanFactory
 *
 * @author cakemonster
 * @date 2023/12/4
 */
public class MapperBeanFactory<T> implements FactoryBean<T> {

    @Override
    public T getObject() throws Exception {
        return null;
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }
}
