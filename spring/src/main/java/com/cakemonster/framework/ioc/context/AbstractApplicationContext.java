package com.cakemonster.framework.ioc.context;

import com.cakemonster.framework.ioc.aware.BeanFactoryAware;
import com.cakemonster.framework.ioc.bean.BeanDefinition;
import com.cakemonster.framework.ioc.factory.AbstractBeanFactory;
import com.cakemonster.framework.ioc.factory.BeanFactory;
import com.cakemonster.framework.ioc.meta.AnnotationMetaData;
import com.cakemonster.framework.ioc.processor.AutowiredAnnotationBeanPostProcessor;
import com.cakemonster.framework.ioc.processor.BeanFactoryPostProcessor;
import com.cakemonster.framework.ioc.processor.BeanPostProcessor;
import com.cakemonster.framework.ioc.processor.ConfigurationClassPostProcessor;
import com.cakemonster.framework.ioc.util.BeanNameUtil;
import com.cakemonster.framework.mvc.handler.RequestMappingHandlerMapping;
import com.google.common.collect.Lists;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * AbstractApplicationContext
 *
 * @author cakemonster
 * @date 2023/11/25
 */
public abstract class AbstractApplicationContext implements ApplicationContext {

    private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors = Lists.newArrayList();

    protected AbstractBeanFactory beanFactory;

    public AbstractApplicationContext(AbstractBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public Object getBean(String name) {
        try {
            return beanFactory.getBean(name);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void refresh() {
        try {
            invokeBeanFactoryPostProcessors(beanFactory);
            // 注册postProcessor
            registerBeanPostProcessors(beanFactory);
            // 实例化
            beanFactory.preInstantiateSingletons();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<BeanFactoryPostProcessor> addFilterConfigurationPostProcessor(AbstractBeanFactory beanFactory)
        throws Exception {
        List<BeanFactoryPostProcessor> result = Lists.newArrayList();
        List<Object> beanPostProcessors = beanFactory.getBeansForType(BeanFactoryPostProcessor.class);
        for (Object beanPostProcessor : beanPostProcessors) {
            if (beanPostProcessor instanceof ConfigurationClassPostProcessor) {
                continue;
            }
            result.add((BeanFactoryPostProcessor)beanPostProcessor);
        }
        return result;
    }

    private void addBeanFactoryPostProcessor(AbstractBeanFactory beanFactory) throws Exception {
        List<Object> beanPostProcessors = beanFactory.getBeansForType(BeanFactoryPostProcessor.class);
        for (Object beanPostProcessor : beanPostProcessors) {
            beanFactoryPostProcessors.add((BeanFactoryPostProcessor)beanPostProcessor);
        }
    }

    private void invokeBeanFactoryPostProcessors(AbstractBeanFactory beanFactory) throws Exception {
        addBeanFactoryPostProcessor(beanFactory);
        for (BeanFactoryPostProcessor beanFactoryPostProcessor : beanFactoryPostProcessors) {
            beanFactoryPostProcessor.postProcessBeanDefinitionRegistry(beanFactory);
        }
        List<BeanFactoryPostProcessor> beanFactoryPostProcessors1 = addFilterConfigurationPostProcessor(beanFactory);
        for (BeanFactoryPostProcessor beanFactoryPostProcessor : beanFactoryPostProcessors1) {
            beanFactoryPostProcessor.postProcessBeanDefinitionRegistry(beanFactory);
        }
    }

    protected void registerBeanPostProcessors(AbstractBeanFactory beanFactory) throws Exception {
        List<Object> beanPostProcessors = beanFactory.getBeansForType(BeanPostProcessor.class);
        for (Object beanPostProcessor : beanPostProcessors) {
            beanFactory.addBeanPostProcessor((BeanPostProcessor)beanPostProcessor);
        }
    }

    private void registerBeanPostProcessorsDefinition(Class<?> clazz) {
        AnnotationMetaData meta = new AnnotationMetaData();
        meta.setClazz(clazz);
        meta.setAnnotations(clazz.getAnnotations());
        meta.setInterfaces(clazz.getInterfaces());
        meta.setBeanClassName(clazz.getName());
        BeanDefinition beanDefinition = new BeanDefinition(meta);
        String beanName = BeanNameUtil.generateBeanName(beanDefinition);
        beanFactory.registerBeanDefinition(beanName, beanDefinition);
    }

}
