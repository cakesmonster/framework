package com.cakemonster.framework.ioc.aware;

import com.cakemonster.framework.ioc.factory.BeanFactory;

/**
 * BeanFactoryAware
 *
 * @author cakemonster
 * @date 2023/11/27
 */
public interface BeanFactoryAware {
    void setBeanFactory(BeanFactory beanFactory) throws Exception;
}
