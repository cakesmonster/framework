package com.cakemonster.framework.ioc.anno;

import java.lang.annotation.*;

/**
 * Autowired
 *
 * @author cakemonster
 * @date 2023/11/19
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Autowired {
    String value() default "";
}
