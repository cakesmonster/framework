package com.cakemonster.framework.ioc;

/**
 * AnnotationConfigApplicationContext
 *
 * @author cakemonster
 * @date 2023/11/25
 */
public class AnnotationConfigApplicationContext implements ApplicationContext {

    private final ClassPathBeanDefinitionScanner classPathBeanDefinitionScanner;

    public AnnotationConfigApplicationContext() {
        this.classPathBeanDefinitionScanner = new ClassPathBeanDefinitionScanner();
    }

    public AnnotationConfigApplicationContext(String basePackage) {
        this();
        classPathBeanDefinitionScanner.scan(basePackage);
        refresh();
    }

    @Override
    public Object getBean(String beanName) {
        return null;
    }

    private void refresh() {

    }
}
