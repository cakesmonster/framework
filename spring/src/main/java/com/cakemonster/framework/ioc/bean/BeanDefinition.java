package com.cakemonster.framework.ioc.bean;

import com.cakemonster.framework.ioc.meta.AnnotationMetaData;
import lombok.Data;

/**
 * BeanDefinition
 *
 * @author cakemonster
 * @date 2023/11/20
 */
@Data
public class BeanDefinition {

    private Object bean;

    private Class<?> beanClass;

    private String beanClassName;

    private AnnotationMetaData metaData;

    private String resource;

    private PropertyValues propertyValues;

    private BeanDefinition() {
    }

    public BeanDefinition(Class<?> clazz) {
        this.propertyValues = new PropertyValues();
        AnnotationMetaData meta = new AnnotationMetaData();
        meta.setClazz(clazz);
        meta.setAnnotations(clazz.getAnnotations());
        meta.setInterfaces(clazz.getInterfaces());
        meta.setBeanClassName(clazz.getName());
        this.metaData = meta;
        setBeanClass(metaData.getClazz());
        setBeanClassName(metaData.getBeanClassName());
        setResource(metaData.getResource());
    }

    public BeanDefinition(AnnotationMetaData metaData) {
        this.propertyValues = new PropertyValues();
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
