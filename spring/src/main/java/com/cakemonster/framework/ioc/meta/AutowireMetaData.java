package com.cakemonster.framework.ioc.meta;

import com.google.common.collect.Lists;
import lombok.Data;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * AutowireMetaData
 *
 * @author cakemonster
 * @date 2023/11/25
 */
@Data
public class AutowireMetaData {

    private Class<?> clazz;

    private List<Field> fields;

    private List<Method> methods;

    public AutowireMetaData() {
        this.fields = Lists.newArrayList();
        this.methods = Lists.newArrayList();
    }
}
