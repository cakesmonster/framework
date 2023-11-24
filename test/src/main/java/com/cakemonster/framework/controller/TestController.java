package com.cakemonster.framework.controller;

import com.cakemonster.framework.ioc.anno.Autowired;
import com.cakemonster.framework.ioc.anno.Controller;

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

}
