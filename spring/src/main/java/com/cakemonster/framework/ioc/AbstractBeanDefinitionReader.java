package com.cakemonster.framework.ioc;

import com.cakemonster.framework.ioc.io.ResourceLoader;
import com.google.common.collect.Maps;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * AbstractBeanDefinitionReader
 *
 * @author cakemonster
 * @date 2023/11/21
 */
@Getter
public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader {

    private final Map<String, BeanDefinition> registry;

    private final ResourceLoader resourceLoader;

    protected AbstractBeanDefinitionReader(ResourceLoader resourceLoader) {
        this.registry = Maps.newConcurrentMap();
        this.resourceLoader = resourceLoader;
    }
}
