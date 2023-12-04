package com.cakemonster.framework.spring;

import com.cakemonster.framework.ioc.bean.BeanDefinition;
import com.cakemonster.framework.ioc.bean.PropertyValue;
import com.cakemonster.framework.ioc.bean.PropertyValues;
import com.cakemonster.framework.ioc.context.ClassPathBeanDefinitionScanner;
import com.cakemonster.framework.ioc.factory.AbstractBeanFactory;
import com.cakemonster.framework.ioc.processor.BeanFactoryPostProcessor;
import com.google.common.base.Strings;

/**
 * MapperScannerConfigurer
 *
 * @author cakemonster
 * @date 2023/12/3
 */
public class MapperScannerConfigurer  implements BeanFactoryPostProcessor {

    private String basePackage;

    public MapperScannerConfigurer() {
    }

    @Override
    public void postProcessBeanDefinitionRegistry(AbstractBeanFactory beanFactory) throws Exception {
        processPropertyPlaceHolders(beanFactory);
        if (Strings.isNullOrEmpty(basePackage)) {
            return;
        }
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(beanFactory);
        scanner.scan(basePackage);
    }

    private void processPropertyPlaceHolders(AbstractBeanFactory beanFactory) {
        BeanDefinition beanDefinition = beanFactory.getRegistry().get("mapperScannerConfigurer");
        if (beanDefinition == null) {
            throw new IllegalArgumentException();
        }
        PropertyValues propertyValues = beanDefinition.getPropertyValues();
        for (PropertyValue propertyValue : propertyValues.getPropertyValues()) {
            if ("basePackage".equals(propertyValue.getName())) {
                this.basePackage = (String)propertyValue.getValue();
            }
        }
    }
}
