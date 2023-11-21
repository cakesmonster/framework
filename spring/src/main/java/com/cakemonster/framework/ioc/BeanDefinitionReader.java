package com.cakemonster.framework.ioc;

/**
 * BeanDefinitionReader
 *
 * @author cakemonster
 * @date 2023/11/21
 */
public interface BeanDefinitionReader {

    void loadBeanDefinitions(String location) throws Exception;
}
