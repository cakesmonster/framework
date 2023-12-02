package com.cakemonster.framework.ioc.anno;

import java.lang.annotation.*;

/**
 * Configuration
 *
 * @author cakemonster
 * @date 2023/12/3
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface Configuration {

    String value() default "";
}
