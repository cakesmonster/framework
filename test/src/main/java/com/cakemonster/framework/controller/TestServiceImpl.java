package com.cakemonster.framework.controller;

import com.cakemonster.framework.ioc.anno.Service;

/**
 * TestService
 *
 * @author cakemonster
 * @date 2023/11/22
 */
@Service
public class TestServiceImpl implements TestService {

    @Override
    public void sayHello() {
        System.out.println("hello");
    }
}
