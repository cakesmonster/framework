package com.cakemonster.framework.ioc.processor;

import com.cakemonster.framework.ioc.bean.BeanDefinition;
import com.cakemonster.framework.ioc.bean.PropertyValue;
import com.cakemonster.framework.ioc.bean.PropertyValues;
import com.cakemonster.framework.ioc.anno.Autowired;
import com.cakemonster.framework.ioc.meta.AutowireMetaData;
import com.cakemonster.framework.ioc.util.BeanNameUtil;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * AutowiredAnnotationBeanPostProcessor
 *
 * @author cakemonster
 * @date 2023/11/25
 */
public class AutowiredAnnotationBeanPostProcessor {

    public void postProcessProperties(Object bean, Map<String, BeanDefinition> registry) {
        AutowireMetaData meta = findAutowiringMetadata(bean.getClass());
        inject(bean, meta, registry);
    }

    private void inject(Object bean, AutowireMetaData meta, Map<String, BeanDefinition> registry) {
        for (Field field : meta.getFields()) {
            Autowired annotation = field.getAnnotation(Autowired.class);
            String beanName = annotation.value();
            if (beanName.trim().isEmpty()) {
                beanName = BeanNameUtil.generateBeanName(field.getType().getName());
            }
            // 开启赋值
            field.setAccessible(true);
            try {
                field.set(bean, registry.get(beanName).getBean());
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private AutowireMetaData findAutowiringMetadata(Class<?> clazz) {
        AutowireMetaData meta = new AutowireMetaData();
        meta.setClazz(clazz);
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            Autowired annotation = declaredField.getAnnotation(Autowired.class);
            if (annotation == null) {
                continue;
            }
            meta.getFields().add(declaredField);
        }
        return meta;
    }

}
