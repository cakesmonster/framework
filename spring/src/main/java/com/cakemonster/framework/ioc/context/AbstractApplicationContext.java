package com.cakemonster.framework.ioc.context;

import com.cakemonster.framework.ioc.factory.BeanFactory;

/**
 * AbstractApplicationContext
 *
 * @author cakemonster
 * @date 2023/11/25
 */
public abstract class AbstractApplicationContext implements ApplicationContext {

    protected BeanFactory beanFactory;

    public AbstractApplicationContext(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void refresh() {
        beanFactory.refresh();
    }

    @Override
    public Object getBean(String name) {
        refresh();
        return beanFactory.getBean(name);
    }
}
