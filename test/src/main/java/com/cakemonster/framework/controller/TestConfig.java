package com.cakemonster.framework.controller;

import com.cakemonster.framework.anno.MapperScan;
import com.cakemonster.framework.ioc.anno.Configuration;

/**
 * TestConfig
 *
 * @author cakemonster
 * @date 2023/12/3
 */
@Configuration
@MapperScan("com.cakemonster.framework.mapper")
public class TestConfig {
}
