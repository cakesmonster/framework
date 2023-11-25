package com.cakemonster.framework.ioc.context;

import com.cakemonster.framework.ioc.bean.BeanDefinition;
import com.cakemonster.framework.ioc.factory.BeanFactory;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * AnnotationConfigApplicationContext
 *
 * @author cakemonster
 * @date 2023/11/25
 */
public class AnnotationConfigApplicationContext extends AbstractApplicationContext {

    private final ClassPathBeanDefinitionScanner classPathBeanDefinitionScanner;

    public AnnotationConfigApplicationContext() {
        super(new BeanFactory());
        this.classPathBeanDefinitionScanner = new ClassPathBeanDefinitionScanner(super.beanFactory.getRegistry());
    }

    public AnnotationConfigApplicationContext(String basePackage) {
        this();
        classPathBeanDefinitionScanner.scan(basePackage);
        refresh();
    }

    @Override
    public Object getBean(String beanName) {
        return super.getBean(beanName);
    }
}
