package com.cakemonster.framework.ioc.context;

import com.cakemonster.framework.ioc.bean.BeanDefinition;
import com.cakemonster.framework.ioc.factory.AbstractBeanFactory;
import com.cakemonster.framework.ioc.meta.AnnotationMetaData;
import com.cakemonster.framework.ioc.processor.AutowiredAnnotationBeanPostProcessor;
import com.cakemonster.framework.ioc.processor.ConfigurationClassPostProcessor;
import com.cakemonster.framework.ioc.util.BeanNameUtil;

import java.util.Map;

/**
 * AnnotatedBeanDefinitionReader
 *
 * @author cakemonster
 * @date 2023/12/3
 */
public class AnnotatedBeanDefinitionReader {

    private static final String CONFIGURATION_ANNOTATION_PROCESSOR_BEAN_NAME =
        "com.cakemonster.framework.ioc.processor.ConfigurationClassPostProcessor";
    private static final String AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME =
        "com.cakemonster.framework.ioc.processor.AutowiredAnnotationBeanPostProcessor";

    public AnnotatedBeanDefinitionReader(AbstractBeanFactory beanFactory) {
        registerAnnotationConfigProcessors(beanFactory);
    }

    private void registerAnnotationConfigProcessors(AbstractBeanFactory beanFactory) {
        Map<String, BeanDefinition> registry = beanFactory.getRegistry();
        if (!registry.containsKey(CONFIGURATION_ANNOTATION_PROCESSOR_BEAN_NAME)) {
            BeanDefinition beanDefinition = new BeanDefinition(ConfigurationClassPostProcessor.class);
            String beanName = BeanNameUtil.generateBeanName(beanDefinition);
            beanFactory.registerBeanDefinition(beanName, beanDefinition);
        }
        if (!registry.containsKey(AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME)) {
            BeanDefinition beanDefinition = new BeanDefinition(AutowiredAnnotationBeanPostProcessor.class);
            String beanName = BeanNameUtil.generateBeanName(beanDefinition);
            beanFactory.registerBeanDefinition(beanName, beanDefinition);
        }
    }

}
