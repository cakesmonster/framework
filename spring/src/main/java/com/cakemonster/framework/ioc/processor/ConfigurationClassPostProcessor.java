package com.cakemonster.framework.ioc.processor;

import com.cakemonster.framework.ioc.anno.Component;
import com.cakemonster.framework.ioc.anno.Configuration;
import com.cakemonster.framework.ioc.anno.Import;
import com.cakemonster.framework.ioc.bean.BeanDefinition;
import com.cakemonster.framework.ioc.factory.AbstractBeanFactory;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * ConfigurationClassPostProcessor
 *
 * @author cakemonster
 * @date 2023/12/3
 */
public class ConfigurationClassPostProcessor implements BeanFactoryPostProcessor {

    private static final Set<Class<? extends Annotation>> CONFIGURATION_ANNO =
        Sets.newHashSet(Configuration.class, Component.class, Import.class);

    private final List<ImportBeanDefinitionRegistrar> importBeanDefinitionRegistrars = Lists.newArrayList();

    @Override
    public void postProcessBeanDefinitionRegistry(AbstractBeanFactory beanFactory) throws Exception {
        for (BeanDefinition beanDefinition : beanFactory.getRegistry().values()) {
            if (!checkConfigurationClassCandidate(beanDefinition)) {
                continue;
            }
            processImports(beanDefinition);
        }
        loadBeanDefinitions(beanFactory);
    }

    private void loadBeanDefinitions(AbstractBeanFactory beanFactory) {
        for (ImportBeanDefinitionRegistrar importBeanDefinitionRegistrar : importBeanDefinitionRegistrars) {
            importBeanDefinitionRegistrar.registerBeanDefinitions(beanFactory);
        }
    }

    private void processImports(BeanDefinition configurationClass) {
        if (!configurationClass.getBeanClass().isAnnotationPresent(Import.class)) {
            return;
        }
        Import impAnno = configurationClass.getBeanClass().getAnnotation(Import.class);
        // TODO(hzq): 目前只单纯处理一个的import的情况，spring是处理数组
        Class<?> clazz = impAnno.value();
        // TODO(hzq): 只处理实现了ImportBeanDefinitionRegistrar接口的类
        boolean assignableFrom = clazz.isAssignableFrom(ImportBeanDefinitionRegistrar.class);
        if (!assignableFrom) {
            return;
        }
        // 实例化
        try {
            ImportBeanDefinitionRegistrar o = (ImportBeanDefinitionRegistrar)clazz.newInstance();
            importBeanDefinitionRegistrars.add(o);
        } catch (Exception e) {
            String msg = "instant " + clazz.getName() + " error";
            throw new RuntimeException(msg, e);
        }
    }

    private boolean checkConfigurationClassCandidate(BeanDefinition beanDefinition) {
        Class<?> beanClass = beanDefinition.getBeanClass();
        for (Class<? extends Annotation> anno : CONFIGURATION_ANNO) {
            if (beanClass.isAnnotationPresent(anno)) {
                return true;
            }
        }
        return false;
    }
}
