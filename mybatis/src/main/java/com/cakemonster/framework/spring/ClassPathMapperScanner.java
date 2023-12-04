package com.cakemonster.framework.spring;

import com.cakemonster.framework.ioc.bean.BeanDefinition;
import com.cakemonster.framework.ioc.context.ClassPathBeanDefinitionScanner;
import com.cakemonster.framework.ioc.factory.AbstractBeanFactory;
import com.cakemonster.framework.ioc.meta.AnnotationMetaData;

import java.lang.annotation.Retention;
import java.util.Set;

/**
 * ClassPathMapperScanner
 *
 * @author cakemonster
 * @date 2023/12/4
 */
public class ClassPathMapperScanner extends ClassPathBeanDefinitionScanner {

    private AbstractBeanFactory beanFactory;

    public ClassPathMapperScanner(AbstractBeanFactory beanFactory) {
        super(beanFactory);
    }

    @Override
    protected Set<BeanDefinition> doScan(String... scanPackages) {
        Set<BeanDefinition> beanDefinitions = super.doScan(scanPackages);
        if (beanDefinitions == null || beanDefinitions.isEmpty()) {
            return beanDefinitions;
        }
        processBeanDefinitions(beanDefinitions);
        return beanDefinitions;
    }

    @Override
    protected boolean isCandidateComponent(AnnotationMetaData metaData) {
        return true;
    }

    private void processBeanDefinitions(Set<BeanDefinition> beanDefinitions) {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            beanDefinition.getMetaData().setClazz(MapperBeanFactory.class);
            beanDefinition.setBeanClass(MapperBeanFactory.class);
        }
    }
}
