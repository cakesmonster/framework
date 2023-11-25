package com.cakemonster.framework.service;

import com.cakemonster.framework.controller.TestController;
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
    }
}
