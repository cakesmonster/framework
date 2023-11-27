package com.cakemonster.framework.ioc.factory;

import com.cakemonster.framework.ioc.aware.BeanFactoryAware;
import com.cakemonster.framework.ioc.bean.BeanDefinition;

/**
 * AutowireCapableBeanFactory
 *
 * @author cakemonster
 * @date 2023/11/27
 */
public class AutowireCapableBeanFactory extends AbstractBeanFactory {
    @Override
    protected void applyPropertyValues(Object bean, BeanDefinition beanDefinition) throws Exception {
        if (bean instanceof BeanFactoryAware) {
            ((BeanFactoryAware)bean).setBeanFactory(this);
        }
        // 好像没啥用，原先是给XML的属性赋值的方法？
    }
}
