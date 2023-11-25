package com.cakemonster.framework.controller;

import com.cakemonster.framework.ioc.anno.Autowired;
import com.cakemonster.framework.ioc.anno.Controller;
import com.cakemonster.framework.ioc.anno.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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

    @RequestMapping("/test")
    public void test() {
        testService.sayHello();
    }

    @RequestMapping("/sayHello")
    public void sayHello(HttpServletResponse resp) throws IOException {
        testService.sayHello();
        resp.getWriter().write("hello");
    }
}
