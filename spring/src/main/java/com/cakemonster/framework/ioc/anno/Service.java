package com.cakemonster.framework.ioc.anno;

import java.lang.annotation.*;

/**
 * Service
 *
 * @author cakemonster
 * @date 2023/11/19
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface Service {
    String value() default "";
}
