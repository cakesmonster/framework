package com.cakemonster.framework.ioc.anno;

import java.lang.annotation.*;

/**
 * Component
 *
 * @author cakemonster
 * @date 2023/11/24
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Component {
    String value() default "";
}
