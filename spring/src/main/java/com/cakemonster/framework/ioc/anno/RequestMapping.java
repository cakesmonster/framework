package com.cakemonster.framework.ioc.anno;

import java.lang.annotation.*;

/**
 * RequestMapping
 *
 * @author cakemonster
 * @date 2023/11/19
 */
@Documented
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {
    String value() default "";
}
