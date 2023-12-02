package com.cakemonster.framework.ioc.context;

import com.cakemonster.framework.ioc.bean.BeanDefinition;
import com.cakemonster.framework.ioc.factory.AutowireCapableBeanFactory;
import com.cakemonster.framework.ioc.factory.BeanFactory;
import com.google.common.collect.Maps;
import lombok.Getter;

import java.util.Map;

/**
 * AnnotationConfigApplicationContext
 *
 * @author cakemonster
 * @date 2023/11/25
 */
@Getter
public class AnnotationConfigApplicationContext extends AbstractApplicationContext {

    private final AnnotatedBeanDefinitionReader annotatedBeanDefinitionReader;

    private final ClassPathBeanDefinitionScanner classPathBeanDefinitionScanner;

    public AnnotationConfigApplicationContext() {
        super(new AutowireCapableBeanFactory());
        this.annotatedBeanDefinitionReader = new AnnotatedBeanDefinitionReader(super.beanFactory);
        this.classPathBeanDefinitionScanner = new ClassPathBeanDefinitionScanner(super.beanFactory);
    }

    public AnnotationConfigApplicationContext(String... basePackages) {
        this();
        classPathBeanDefinitionScanner.scan(basePackages);
        refresh();
    }

    @Override
    public Object getBean(String beanName) {
        return super.getBean(beanName);
    }
}
