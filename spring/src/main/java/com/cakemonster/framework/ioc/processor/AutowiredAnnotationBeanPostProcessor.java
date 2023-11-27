package com.cakemonster.framework.ioc.processor;

import com.cakemonster.framework.ioc.aware.BeanFactoryAware;
import com.cakemonster.framework.ioc.anno.Autowired;
import com.cakemonster.framework.ioc.factory.AbstractBeanFactory;
import com.cakemonster.framework.ioc.factory.BeanFactory;
import com.cakemonster.framework.ioc.meta.AutowireMetaData;
import com.cakemonster.framework.ioc.util.BeanNameUtil;

import java.lang.reflect.Field;

/**
 * AutowiredAnnotationBeanPostProcessor
 *
 * @author cakemonster
 * @date 2023/11/25
 */
public class AutowiredAnnotationBeanPostProcessor implements BeanPostProcessor, BeanFactoryAware {

    private AbstractBeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws Exception {
        this.beanFactory = (AbstractBeanFactory)beanFactory;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
        postProcessProperties(bean);
        return bean;
    }

    public void postProcessProperties(Object bean) throws Exception {
        AutowireMetaData meta = findAutowiringMetadata(bean.getClass());
        inject(bean, meta);
    }

    private void inject(Object bean, AutowireMetaData meta) throws Exception {
        for (Field field : meta.getFields()) {
            Autowired annotation = field.getAnnotation(Autowired.class);
            String beanName = annotation.value();
            if (beanName.trim().isEmpty()) {
                beanName = BeanNameUtil.generateBeanName(field.getType().getName());
            }
            // 开启赋值
            field.setAccessible(true);
            field.set(bean, beanFactory.getBean(beanName));
        }
    }

    private AutowireMetaData findAutowiringMetadata(Class<?> clazz) {
        AutowireMetaData meta = new AutowireMetaData();
        meta.setClazz(clazz);
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            if (!declaredField.isAnnotationPresent(Autowired.class)) {
                continue;
            }
            meta.getFields().add(declaredField);
        }
        return meta;
    }

}
