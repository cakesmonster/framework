package com.cakemonster.framework.spring;

import com.cakemonster.framework.anno.MapperScan;
import com.cakemonster.framework.ioc.bean.BeanDefinition;
import com.cakemonster.framework.ioc.bean.PropertyValue;
import com.cakemonster.framework.ioc.factory.AbstractBeanFactory;
import com.cakemonster.framework.ioc.meta.AnnotationAttributes;
import com.cakemonster.framework.ioc.meta.AnnotationMetaData;
import com.cakemonster.framework.ioc.processor.ImportBeanDefinitionRegistrar;
import com.cakemonster.framework.ioc.util.BeanNameUtil;

/**
 * MapperScannerRegistrar
 *
 * @author cakemonster
 * @date 2023/12/3
 */
public class MapperScannerRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetaData metaData, AbstractBeanFactory beanFactory) {
        AnnotationAttributes annotationAttributes = metaData.getAttributesMap().get(MapperScan.class.getName());
        String value = (String)annotationAttributes.get("value");
        BeanDefinition beanDefinition = new BeanDefinition(MapperScannerConfigurer.class);
        beanDefinition.getPropertyValues().addPropertyValue(new PropertyValue("basePackage", value));
        String beanName = BeanNameUtil.generateBeanName(beanDefinition);
        beanFactory.registerBeanDefinition(beanName, beanDefinition);
    }
}
