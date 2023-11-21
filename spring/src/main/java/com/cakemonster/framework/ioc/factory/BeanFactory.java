package com.cakemonster.framework.ioc.factory;

import com.cakemonster.framework.ioc.BeanDefinition;
import com.cakemonster.framework.ioc.PropertyValue;
import com.google.common.collect.Maps;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * BeanFactory
 *
 * @author cakemonster
 * @date 2023/11/20
 */
public class BeanFactory {

    private final Map<String, BeanDefinition> beanDefinitionMap = Maps.newConcurrentMap();

    public Object getBean(String name) {
        return beanDefinitionMap.get(name).getBean();
    }

    public void registerBeanDefinition(String name, BeanDefinition beanDefinition)
        throws NoSuchFieldException, IllegalAccessException {
        Object bean = doCreateBean(beanDefinition);
        applyPropertyValues(bean, beanDefinition);
        beanDefinition.setBean(bean);
        beanDefinitionMap.put(name, beanDefinition);
    }

    private void applyPropertyValues(Object bean, BeanDefinition beanDefinition)
        throws NoSuchFieldException, IllegalAccessException {
        for (PropertyValue propertyValue : beanDefinition.getPropertyValues()) {
            Field declaredField = bean.getClass().getDeclaredField(propertyValue.getName());
            declaredField.setAccessible(true);
            declaredField.set(bean, propertyValue.getValue());
        }
    }

    private Object doCreateBean(BeanDefinition beanDefinition) {
        Object beanInstance = null;
        try {
            beanInstance = beanDefinition.getBeanClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return beanInstance;
    }
}
