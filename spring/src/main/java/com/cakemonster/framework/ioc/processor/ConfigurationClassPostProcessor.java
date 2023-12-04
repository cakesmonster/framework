package com.cakemonster.framework.ioc.processor;

import com.cakemonster.framework.ioc.anno.Component;
import com.cakemonster.framework.ioc.anno.Configuration;
import com.cakemonster.framework.ioc.anno.Import;
import com.cakemonster.framework.ioc.bean.BeanDefinition;
import com.cakemonster.framework.ioc.factory.AbstractBeanFactory;
import com.cakemonster.framework.ioc.meta.AnnotationMetaData;
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

    @Override
    public void postProcessBeanDefinitionRegistry(AbstractBeanFactory beanFactory) throws Exception {
        Collection<BeanDefinition> beanDefinitions = beanFactory.getRegistry().values();
        for (BeanDefinition beanDefinition : beanDefinitions) {
            if (!checkConfigurationClassCandidate(beanDefinition)) {
                continue;
            }
            ImportBeanDefinitionRegistrar importBeanDefinitionRegistrar = processImports(beanDefinition);
            if (importBeanDefinitionRegistrar == null) {
                continue;
            }
            loadBeanDefinitions(beanDefinition, beanFactory, importBeanDefinitionRegistrar);
        }
    }

    private void loadBeanDefinitions(BeanDefinition beanDefinition, AbstractBeanFactory beanFactory,
        ImportBeanDefinitionRegistrar importBeanDefinitionRegistrar) {
        AnnotationMetaData metaData = beanDefinition.getMetaData();
        importBeanDefinitionRegistrar.registerBeanDefinitions(metaData, beanFactory);
    }

    private ImportBeanDefinitionRegistrar processImports(BeanDefinition configurationClass) {
        Annotation importAnno = getImportAnno(configurationClass.getMetaData());
        if (importAnno == null) {
            return null;
        }
        Import impAnno = (Import)importAnno;
        // TODO(hzq): 目前只单纯处理一个的import的情况，spring是处理数组
        Class<?> clazz = impAnno.value();
        // TODO(hzq): 只处理实现了ImportBeanDefinitionRegistrar接口的类
        boolean assignableFrom = ImportBeanDefinitionRegistrar.class.isAssignableFrom(clazz);
        if (!assignableFrom) {
            return null;
        }
        // 实例化
        try {
            return (ImportBeanDefinitionRegistrar)clazz.newInstance();
        } catch (Exception e) {
            String msg = "instant " + clazz.getName() + " error";
            throw new RuntimeException(msg, e);
        }
    }

    private Annotation getImportAnno(AnnotationMetaData metaData) {
        Annotation[] annotations = metaData.getAnnotations();
        // 遍历注解，检查是否包含@Import或@Import的元注解
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().equals(Import.class)) {
                return annotation;
            }
            boolean isPresent = annotation.annotationType().isAnnotationPresent(Import.class);
            if (isPresent) {
                return annotation.annotationType().getAnnotation(Import.class);
            }
        }
        return null;
    }

    private boolean checkHasImportAnnotation(AnnotationMetaData metaData) {
        Annotation[] annotations = metaData.getAnnotations();
        // 遍历注解，检查是否包含@Import或@Import的元注解
        for (Annotation annotation : annotations) {
            if (isImportAnnotation(annotation.annotationType())) {
                return true;
            }
        }
        return false;
    }

    private boolean isImportAnnotation(Class<? extends Annotation> annotationType) {
        // 注解本身是否是@Import
        if (annotationType.equals(Import.class)) {
            return true;
        }
        // 检查注解类上是否有@Import注解
        Import annotation = annotationType.getAnnotation(Import.class);
        return annotation != null;
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
