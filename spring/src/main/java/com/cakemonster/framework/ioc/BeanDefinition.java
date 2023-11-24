package com.cakemonster.framework.ioc;

import com.google.common.collect.Lists;
import lombok.Data;

import java.io.File;
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

    private AnnotationMetaData metaData;

    private String resource;

    private List<PropertyValue> propertyValues = Lists.newArrayList();

    public BeanDefinition() {
    }

    public BeanDefinition(AnnotationMetaData metaData) {
        this.metaData = metaData;
        setBeanClass(metaData.getClazz());
        setBeanClassName(metaData.getBeanClassName());
        setResource(metaData.getResource());
    }

    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
        try {
            this.beanClass = Class.forName(beanClassName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
