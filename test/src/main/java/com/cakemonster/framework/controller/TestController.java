package com.cakemonster.framework.controller;

import com.cakemonster.framework.ioc.anno.Autowired;
import com.cakemonster.framework.ioc.anno.Controller;
import com.cakemonster.framework.ioc.anno.RequestMapping;

/**
 * Controller
 *
 * @author cakemonster
 * @date 2023/11/22
 */
@Controller
public class TestController {

    @Autowired
    private TestService testService;

    @RequestMapping("/sayHello")
    public void sayHello() {
        testService.sayHello();
    }
}
