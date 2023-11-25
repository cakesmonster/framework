package com.cakemonster.framework.ioc.meta;

import lombok.Data;

import java.lang.annotation.Annotation;

/**
 * AnnotationMetaData
 *
 * @author cakemonster
 * @date 2023/11/24
 */
@Data
public class AnnotationMetaData {

    private String beanClassName;

    private Class<?> clazz;

    private Class<?>[] interfaces;

    private Annotation[] annotations;

    private String resource;
}
