package com.cakemonster.framework.service;

import com.cakemonster.framework.ioc.context.AnnotationConfigApplicationContext;
import com.cakemonster.framework.ioc.context.ApplicationContext;
import com.cakemonster.framework.ioc.factory.AutowireCapableBeanFactory;
import com.cakemonster.framework.spring.ClassPathMapperScanner;

/**
 * TestMybatisSpring
 *
 * @author cakemonster
 * @date 2023/12/4
 */
public class TestMybatisSpring {

    public static void main(String[] args) {
        ClassPathMapperScanner scanner = new ClassPathMapperScanner(new AutowireCapableBeanFactory());
        scanner.scan("com.cakemonster.framework.mapper");
    }
}
