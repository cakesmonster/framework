package com.cakemonster.framework.ioc.meta;

import com.google.common.collect.Lists;
import lombok.Data;

import java.lang.annotation.Annotation;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * AnnotationAttributes
 *
 * @author cakemonster
 * @date 2023/12/3
 */
@Data
public class AnnotationAttributes extends LinkedHashMap<String, Object> {

    private Class<? extends Annotation> annotation;

}
