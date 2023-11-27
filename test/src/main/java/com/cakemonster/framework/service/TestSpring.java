package com.cakemonster.framework.service;

import com.cakemonster.framework.aop.AdvisedSupport;
import com.cakemonster.framework.aop.JdkAopProxy;
import com.cakemonster.framework.aop.TargetSource;
import com.cakemonster.framework.aop.TransactionInterceptor;
import com.cakemonster.framework.controller.TestController;
import com.cakemonster.framework.controller.TestService;
import com.cakemonster.framework.ioc.context.AnnotationConfigApplicationContext;
import com.cakemonster.framework.ioc.context.ApplicationContext;

/**
 * TestSpring
 *
 * @author cakemonster
 * @date 2023/11/25
 */
public class TestSpring {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext("com.cakemonster.framework.controller");
        TestController testController = (TestController)context.getBean("testController");
        testController.test();

        TestService testService = (TestService)context.getBean("testService");
        testService.sayHello();

        AdvisedSupport advisedSupport = new AdvisedSupport();
        TargetSource targetSource = new TargetSource(TestService.class, testService);
        advisedSupport.setTargetSource(targetSource);
        advisedSupport.setMethodInterceptor(new TransactionInterceptor());

        JdkAopProxy jdkAopProxy = new JdkAopProxy(advisedSupport);
        TestService proxy = (TestService)jdkAopProxy.getProxy();
        proxy.sayHello();
    }
}
