package com.cakemonster.framework.ioc;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
 * BeanDefinition
 *
 * @author cakemonster
 * @date 2023/11/20
 */
@Data
public class BeanDefinition {

    private Object bean;

    private Class beanClass;

    private String beanClassName;

    private List<PropertyValue> propertyValues = Lists.newArrayList();

    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
        try {
            this.beanClass = Class.forName(beanClassName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
