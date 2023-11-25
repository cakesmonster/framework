package com.cakemonster.framework.ioc.factory;

import com.cakemonster.framework.ioc.bean.BeanDefinition;
import com.cakemonster.framework.ioc.bean.PropertyValue;
import com.cakemonster.framework.ioc.processor.AutowiredAnnotationBeanPostProcessor;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * BeanFactory
 *
 * @author cakemonster
 * @date 2023/11/20
 */
@Getter
public class BeanFactory {

    private final Map<String, BeanDefinition> registry = Maps.newConcurrentMap();

    private final AutowiredAnnotationBeanPostProcessor autowiredAnnotationBeanPostProcessor;

    public BeanFactory() {
        this.autowiredAnnotationBeanPostProcessor = new AutowiredAnnotationBeanPostProcessor();
    }

    public Object getBean(String name) {
        BeanDefinition beanDefinition = registry.get(name);
        Preconditions.checkState(beanDefinition != null, "No bean name is " + name + " that undefined");
        return beanDefinition.getBean();
    }

    public void refresh() {
        instantiateSingleton();
        autowired();
    }

    private void autowired() {
        for (Map.Entry<String, BeanDefinition> beanDefinitionEntry : registry.entrySet()) {
            BeanDefinition beanDefinition = beanDefinitionEntry.getValue();
            autowiredAnnotationBeanPostProcessor.postProcessProperties(beanDefinition.getBean(), registry);
        }
    }

    private void instantiateSingleton() {
        for (Map.Entry<String, BeanDefinition> beanDefinitionEntry : registry.entrySet()) {
            BeanDefinition beanDefinition = beanDefinitionEntry.getValue();
            try {
                createBean(beanDefinition);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void createBean(BeanDefinition beanDefinition) throws NoSuchFieldException, IllegalAccessException {
        Object bean = doCreateBean(beanDefinition);
        // applyPropertyValues(bean, beanDefinition);
        beanDefinition.setBean(bean);
    }

    private void applyPropertyValues(Object bean, BeanDefinition beanDefinition)
        throws NoSuchFieldException, IllegalAccessException {
        for (PropertyValue propertyValue : beanDefinition.getPropertyValues().getPropertyValues()) {
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
